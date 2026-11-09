import { describe, expect, it, vi } from 'vitest'
import { ApiError, changeBag, createApi, normalizeProduct, readBag, safeImageUrl } from './store'

const raw = {
  productId: 7,
  name: 'Cleanser',
  price: '199.95',
  stockQuantity: 4,
  hairConcern: null,
  imageUrl: 'https://example.com/product.png',
}
describe('Existing backend contracts', () => {
  it('normalizes decimal prices and recognizes subtype fields even when null', () => {
    expect(normalizeProduct(raw)).toMatchObject({
      productId: 7,
      price: 199.95,
      category: 'hair',
      stockQuantity: 4,
      reviews: [],
    })
    expect(
      normalizeProduct({ ...raw, hairConcern: undefined, skinConcern: 'Dryness' }).category,
    ).toBe('hair')
    expect(
      normalizeProduct({
        productId: 2,
        name: 'Body cream',
        price: 20,
        stockQuantity: 0,
        skinConcern: null,
      }).category,
    ).toBe('body')
  })
  it('rejects missing and invalid prices instead of treating them as free products', () => {
    for (const price of [null, undefined, '', -1, 'not-a-price', Infinity])
      expect(() => normalizeProduct({ ...raw, price })).toThrow()
  })
  it('uses the existing catalogue path and accepts an empty catalogue', async () => {
    const fetcher = vi.fn<typeof fetch>().mockResolvedValue(new Response('[]', { status: 200 }))
    expect(await createApi('http://localhost:8080/', fetcher).products()).toEqual([])
    expect(fetcher.mock.calls[0][0]).toBe('http://localhost:8080/product/getAll')
  })
  it('handles a missing order returned as HTTP 200 with a null body', async () => {
    const fetcher = vi.fn<typeof fetch>().mockResolvedValue(new Response('null', { status: 200 }))
    await expect(createApi('/api', fetcher).order('999')).rejects.toMatchObject({ status: 404 })
  })
  it('validates order identifiers before making a request', async () => {
    const fetcher = vi.fn<typeof fetch>()
    for (const id of ['0', '-1', '../customer/getAll', '1.5', '999999999999999999'])
      await expect(createApi('/api', fetcher).order(id)).rejects.toBeInstanceOf(ApiError)
    expect(fetcher).not.toHaveBeenCalled()
  })
  it('shows useful errors for network failures, malformed JSON, and server errors', async () => {
    const fetcher = vi
      .fn<typeof fetch>()
      .mockRejectedValueOnce(new TypeError('Network error'))
      .mockResolvedValueOnce(new Response('<html>error</html>', { status: 200 }))
      .mockResolvedValueOnce(new Response('Internal error', { status: 500 }))
    const api = createApi('/api', fetcher)
    await expect(api.products()).rejects.toThrow('can’t reach')
    await expect(api.products()).rejects.toThrow('could not load')
    await expect(api.products()).rejects.toMatchObject({ status: 500 })
  })
  it('does not expose customer credentials in its order model', async () => {
    const fetcher = vi.fn<typeof fetch>().mockResolvedValue(
      new Response(
        JSON.stringify({
          orderId: 1,
          totalAmount: 20,
          status: 'PENDING',
          orderDate: '2026-09-09',
          customer: { email: 'private@example.com', password: 'private' },
          orderItems: [],
        }),
      ),
    )
    const order = await createApi('/api', fetcher).order('1')
    expect(order).not.toHaveProperty('customer')
    expect(fetcher.mock.calls[0][0]).toBe('/api/order/read/1')
  })
})
describe('Saved shopping bag', () => {
  it('recovers from corrupt storage, duplicates, and invalid quantities', () => {
    expect(readBag('{broken')).toEqual([])
    expect(readBag('{}')).toEqual([])
    expect(
      readBag(
        JSON.stringify([
          { productId: 1, quantity: 120 },
          { productId: 1, quantity: 1 },
          null,
          { productId: 2, quantity: -4 },
          { productId: 3, quantity: 1.5 },
        ]),
      ),
    ).toEqual([{ productId: 1, quantity: 99 }])
  })
  it('caps additions at available stock, updates existing lines, and removes zero quantities', () => {
    const first = changeBag([], 4, 8, 3)
    expect(first).toEqual([{ productId: 4, quantity: 3 }])
    expect(changeBag(first, 4, 2, 3)).toEqual([{ productId: 4, quantity: 2 }])
    expect(changeBag(first, 4, 0, 3)).toEqual([])
    expect(changeBag(first, 5, 1, 0)).toEqual(first)
  })
  it('ignores unsafe image schemes', () => {
    expect(safeImageUrl('javascript:alert(1)')).toBe('')
    expect(safeImageUrl('//example.com/image')).toBe('')
    expect(safeImageUrl('/images/product.svg')).toBe('/images/product.svg')
  })
  it('repairs the three legacy catalogue image URLs with bundled product illustrations', () => {
    for (const [file, asset] of [
      ['serum', 'radiantskin-hydrating-facial-serum-30ml'],
      ['hairmask', 'radiantskin-repairing-hair-mask-250ml'],
      ['lotion', 'radiantskin-soothing-body-lotion-400ml'],
    ]) {
      expect(
        normalizeProduct({ ...raw, imageUrl: `https://example.com/images/${file}.jpg` }).imageUrl,
      ).toBe(`/images/products/${asset}.svg`)
    }
    expect(safeImageUrl('https://example.com/unknown.jpg')).toBe('')
    expect(safeImageUrl(' https://cdn.store.co.za/product.jpg ')).toBe(
      'https://cdn.store.co.za/product.jpg',
    )
  })
})
