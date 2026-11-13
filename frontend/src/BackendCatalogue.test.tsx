import { withCartServer } from './test/cartServer'
import { afterEach, beforeEach, expect, it, vi } from 'vitest'
import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

const fetcher = vi.fn<typeof fetch>()
const liveProduct = {
  productId: 999,
  name: 'Backend-only product',
  price: 123,
  stockQuantity: 1,
  usageInstructions: 'Follow the pack instructions.',
}

beforeEach(() => {
  localStorage.clear()
  window.history.replaceState({}, '', '/shop')
  vi.stubGlobal('scrollTo', vi.fn())
  vi.stubGlobal('fetch', withCartServer(fetcher.mockReset()))
})
afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})

it('shows an unavailable catalogue without substituting products when the backend is down', async () => {
  fetcher.mockRejectedValue(new TypeError('Connection refused'))
  render(<App />)
  expect((await screen.findByRole('alert')).textContent).toContain('reach the store')
  expect(document.querySelectorAll('.product-card')).toHaveLength(0)
  expect(fetcher).toHaveBeenCalledWith(
    '/api/product/getAll',
    expect.objectContaining({ cache: 'no-store' }),
  )
})

it('removes previously loaded products when the backend fails and recovers from the backend', async () => {
  const user = userEvent.setup()
  fetcher.mockResolvedValueOnce(new Response(JSON.stringify([liveProduct])))
  render(<App />)
  await screen.findByRole('link', { name: 'Backend-only product' })
  fetcher.mockRejectedValue(new TypeError('Connection refused'))
  fireEvent(window, new Event('focus'))
  await screen.findByRole('alert')
  expect(screen.queryByRole('link', { name: 'Backend-only product' })).toBeNull()
  expect(document.querySelectorAll('.product-card')).toHaveLength(0)
  fetcher.mockResolvedValue(new Response(JSON.stringify([liveProduct])))
  await user.click(screen.getByRole('button', { name: 'Try again' }))
  await screen.findByRole('link', { name: 'Backend-only product' })
  await waitFor(() => expect(screen.queryByRole('alert')).toBeNull())
  expect(JSON.stringify(localStorage)).not.toContain('Backend-only product')
})

it('preserves an empty database response without adding a sample catalogue', async () => {
  fetcher.mockResolvedValue(new Response('[]'))
  render(<App />)
  await screen.findByRole('heading', { name: 'Our shelves are being prepared' })
  expect(document.querySelectorAll('.product-card')).toHaveLength(0)
})
