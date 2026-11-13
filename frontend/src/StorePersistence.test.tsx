import { afterEach, beforeEach, expect, it, vi } from 'vitest'
import { cleanup, render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'
import type { BagItem } from './lib/store'

const product = { productId: 1, name: 'Daily Cream', price: 245, stockQuantity: 5 }
let saved: BagItem[]
let failSave: boolean
const fetcher = vi.fn<typeof fetch>()
function open() {
  window.history.replaceState({}, '', '/shop')
  return render(<App />)
}
beforeEach(() => {
  localStorage.clear()
  saved = []
  failSave = false
  vi.stubGlobal('scrollTo', vi.fn())
  fetcher.mockReset().mockImplementation(async (url, init) => {
    if (String(url).endsWith('/auth/session')) return new Response(null, { status: 401 })
    if (String(url).endsWith('/storefront/cart')) {
      if (init?.method === 'PUT') {
        if (failSave) return new Response(null, { status: 500 })
        saved = JSON.parse(String(init.body)) as BagItem[]
      }
      return new Response(JSON.stringify(saved))
    }
    return new Response(JSON.stringify([product]))
  })
  vi.stubGlobal('fetch', fetcher)
})
afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})
it('saves changes through the API and restores them after reopening the app', async () => {
  const user = userEvent.setup()
  const first = open()
  const add = await screen.findByRole('button', { name: 'Add Daily Cream to bag' })
  await waitFor(() => expect((add as HTMLButtonElement).disabled).toBe(false))
  await user.click(add)
  await screen.findByRole('link', { name: 'Shopping bag, 1 items' })
  expect(saved).toEqual([{ productId: 1, quantity: 1 }])
  expect(localStorage.getItem('radiantskin.bag.v1')).toBeNull()
  first.unmount()
  open()
  expect(await screen.findByRole('link', { name: 'Shopping bag, 1 items' })).toBeTruthy()
})
it('preserves the saved bag when saving fails and permits retrying the change', async () => {
  const user = userEvent.setup()
  saved = [{ productId: 1, quantity: 1 }]
  failSave = true
  open()
  const add = await screen.findByRole('button', { name: 'Add Daily Cream to bag' })
  await waitFor(() => expect((add as HTMLButtonElement).disabled).toBe(false))
  await user.click(add)
  expect((await screen.findByRole('alert')).textContent).toContain('not saved')
  expect(saved).toEqual([{ productId: 1, quantity: 1 }])
  expect(screen.getByRole('link', { name: 'Shopping bag, 1 items' })).toBeTruthy()
  failSave = false
  await user.click(add)
  expect(await screen.findByRole('link', { name: 'Shopping bag, 2 items' })).toBeTruthy()
  expect(saved).toEqual([{ productId: 1, quantity: 2 }])
})
it('blocks writes when loading the saved bag fails and offers recovery', async () => {
  fetcher.mockImplementation(async (url) => {
    if (String(url).endsWith('/storefront/cart')) return new Response(null, { status: 500 })
    if (String(url).endsWith('/auth/session')) return new Response(null, { status: 401 })
    return new Response(JSON.stringify([product]))
  })
  open()
  expect((await screen.findByRole('alert')).textContent).toContain('could not be loaded')
  const user = userEvent.setup()
  await user.click(await screen.findByRole('button', { name: 'Add Daily Cream to bag' }))
  expect(fetcher.mock.calls.some(([, init]) => init?.method === 'PUT')).toBe(false)
  expect(screen.getByRole('button', { name: 'Try again' })).toBeTruthy()
})
