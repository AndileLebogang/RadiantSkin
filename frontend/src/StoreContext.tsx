import { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react'
import type { ReactNode } from 'react'
import { changeBag, createApi, readBag } from './lib/store'
import type { BagItem, Product } from './lib/store'
import { useAuth } from './AuthContext'

const api = createApi(import.meta.env.VITE_API_BASE_URL || '/api')
const bagKey = 'radiantskin.bag.v1'
type Store = {
  products: Product[]
  loading: boolean
  error: string
  reload: () => void
  bag: BagItem[]
  setQuantity: (product: Product, quantity: number) => void
  remove: (productId: number) => void
  clearBag: () => void
  count: number
  notice: string
  bagError: string
  bagBusy: boolean
  retryBag: () => void
}
const Context = createContext<Store | null>(null)
export function StoreProvider({ children }: { children: ReactNode }) {
  const { user, loading: accountLoading } = useAuth()
  const accountId = user?.userId
  const [bagBusy, setBagBusy] = useState(true)
  const scope = accountId ? `account:${accountId}` : 'guest'
  const [bagScope, setBagScope] = useState('loading')
  const [bagError, setBagError] = useState('')
  const [bagVersion, setBagVersion] = useState(0)
  const bagRef = useRef<BagItem[]>([])
  const queue = useRef<Promise<unknown>>(Promise.resolve())
  const generation = useRef(0)
  const ready = useRef(false)
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [version, setVersion] = useState(0)
  const [notice, setNotice] = useState('')
  const [bag, setBag] = useState<BagItem[]>([])
  useEffect(() => {
    const controller = new AbortController()
    api
      .products(controller.signal)
      .then((result) => {
        if (!controller.signal.aborted) {
          setProducts(result)
          setError('')
        }
      })
      .catch((error) => {
        if (!controller.signal.aborted) {
          setProducts([])
          setError(error instanceof Error ? error.message : 'The catalogue could not be loaded.')
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false)
      })
    return () => controller.abort()
  }, [version])
  useEffect(() => {
    const refresh = () => setVersion((value) => value + 1)
    const onVisible = () => {
      if (document.visibilityState === 'visible') refresh()
    }
    const timer = window.setInterval(refresh, 30000)
    window.addEventListener('focus', refresh)
    window.addEventListener('online', refresh)
    document.addEventListener('visibilitychange', onVisible)
    return () => {
      window.clearInterval(timer)
      window.removeEventListener('focus', refresh)
      window.removeEventListener('online', refresh)
      document.removeEventListener('visibilitychange', onVisible)
    }
  }, [])
  useEffect(() => {
    if (accountLoading) return
    const current = ++generation.current
    ready.current = false
    queue.current = queue.current
      .catch(() => {})
      .then(async () => {
        if (generation.current !== current) return
        setBagBusy(true)
        bagRef.current = []
        setBag([])
        setBagError('')
        try {
          let saved = await api.bag()
          if (generation.current !== current) return
          // Import the old device-only guest bag once. Account bags always come from the server.
          let cached: BagItem[] = []
          try {
            cached = readBag(localStorage.getItem(bagKey))
          } catch {
            /* storage unavailable */
          }
          if (!accountId && !saved.length && cached.length) saved = await api.saveBag(cached)
          if (generation.current !== current) return
          bagRef.current = saved
          setBag(saved)
          setBagScope(scope)
          ready.current = true
          try {
            localStorage.removeItem(bagKey)
          } catch {
            /* storage unavailable */
          }
        } catch {
          if (generation.current === current)
            setBagError('Your bag could not be loaded. Try again before changing it.')
        } finally {
          if (generation.current === current) {
            setBagBusy(false)
            setBagScope(scope)
          }
        }
      })
    return () => {
      generation.current = current + 1
      ready.current = false
    }
  }, [accountLoading, accountId, bagVersion, scope])

  function updateBag(change: (current: BagItem[]) => BagItem[], message = 'Bag saved') {
    const current = generation.current
    setBagBusy(true)
    queue.current = queue.current
      .catch(() => {})
      .then(async () => {
        if (generation.current !== current) return
        if (!ready.current) {
          setBagError('Your bag could not be loaded. Try again before changing it.')
          setBagBusy(false)
          return
        }
        try {
          const saved = await api.saveBag(change(bagRef.current))
          if (generation.current !== current) return
          bagRef.current = saved
          setBag(saved)
          setBagError('')
          setNotice(message)
        } catch {
          if (generation.current === current)
            setBagError('Your bag change was not saved. Try the change again.')
        } finally {
          if (generation.current === current) setBagBusy(false)
        }
      })
  }
  useEffect(() => {
    if (!notice) return
    const timer = window.setTimeout(() => setNotice(''), 3500)
    return () => window.clearTimeout(timer)
  }, [notice])
  const reload = useCallback(() => {
    setLoading(true)
    setError('')
    setVersion((value) => value + 1)
  }, [])
  function setQuantity(product: Product, quantity: number) {
    updateBag(
      (current) => changeBag(current, product.productId, quantity, product.stockQuantity),
      `${product.name} · bag updated`,
    )
  }
  return (
    <Context.Provider
      value={{
        products,
        loading,
        error,
        reload,
        bag: bagScope === scope && !accountLoading ? bag : [],
        setQuantity,
        remove: (id) => updateBag((current) => current.filter((item) => item.productId !== id)),
        clearBag: () => {
          bagRef.current = []
          setBag([])
          setNotice('')
        },
        count: (bagScope === scope && !accountLoading ? bag : []).reduce(
          (sum, item) => sum + item.quantity,
          0,
        ),
        notice,
        bagError,
        bagBusy: bagBusy || bagScope !== scope || accountLoading,
        retryBag: () => setBagVersion((value) => value + 1),
      }}
    >
      {children}
    </Context.Provider>
  )
}
// eslint-disable-next-line react-refresh/only-export-components
export function useStore() {
  const context = useContext(Context)
  if (!context) throw new Error('StoreProvider is missing')
  return context
}
