import type { BagItem } from '../lib/store'

// Keep cart responses separate from catalogue/auth fixtures, as the real API does.
export function withCartServer(fetcher: typeof fetch): typeof fetch {
  let bag: BagItem[] = []
  return async (url, init) => {
    if (String(url).endsWith('/storefront/cart')) {
      if (init?.method === 'PUT') bag = JSON.parse(String(init.body)) as BagItem[]
      return new Response(JSON.stringify(bag))
    }
    const response = await fetcher(url, init)
    if (String(url).endsWith('/checkout/place') && response.ok) bag = []
    return response
  }
}

export async function savedBag(): Promise<BagItem[]> {
  return (await fetch('/api/storefront/cart')).json() as Promise<BagItem[]>
}
