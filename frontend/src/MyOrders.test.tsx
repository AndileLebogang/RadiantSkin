import { withCartServer } from './test/cartServer'
import { afterEach, beforeEach, expect, it, vi } from 'vitest'
import { cleanup, render, screen, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

const customer = {
  userId: 9,
  firstName: 'Sam',
  lastName: 'Mokoena',
  email: 'sam@example.com',
  phoneNumber: '0712345678',
  role: 'CUSTOMER',
}
const orders = [
  {
    orderId: 42,
    orderDate: '2026-11-13T12:00:00',
    status: 'PROCESSING',
    totalAmount: 490,
    orderItems: [{ orderItemId: 1, quantity: 2, unitPrice: 245, product: { name: 'Daily Cream' } }],
  },
  {
    orderId: 12,
    orderDate: '2026-10-01T12:00:00',
    status: 'DELIVERED',
    totalAmount: 100,
    orderItems: [{ orderItemId: 2, quantity: 1, unitPrice: 100, product: { name: 'Body Lotion' } }],
  },
]
const fetcher = vi.fn<typeof fetch>()
function open(path = '/my-orders') {
  window.history.replaceState({}, '', path)
  render(<App />)
}
beforeEach(() => {
  localStorage.clear()
  vi.stubGlobal('scrollTo', vi.fn())
  fetcher.mockReset().mockImplementation(async (url) => {
    const path = String(url)
    if (path.endsWith('/auth/session')) return new Response(JSON.stringify(customer))
    if (path.endsWith('/order/mine')) return new Response(JSON.stringify(orders))
    return new Response('[]')
  })
  vi.stubGlobal('fetch', withCartServer(fetcher))
})
afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})

it('opens purchase history from the account and shows orders with quantities and totals', async () => {
  const user = userEvent.setup()
  open('/account')
  await user.click(await within(screen.getByRole('main')).findByRole('link', { name: 'My orders' }))
  const cards = await screen.findAllByRole('article')
  expect(within(cards[0]).getByRole('heading', { name: 'Order #42' })).toBeTruthy()
  expect(within(cards[0]).getByText('Daily Cream')).toBeTruthy()
  expect(within(cards[0]).getByText(/Qty 2/)).toBeTruthy()
  expect(within(cards[0]).getByText('13 November 2026')).toBeTruthy()
  expect(within(cards[0]).getAllByText(/490/)).toHaveLength(2)
  expect(within(cards[1]).getByText('Body Lotion')).toBeTruthy()
  const call = fetcher.mock.calls.find(([url]) => String(url).endsWith('/order/mine'))
  expect(call?.[1]).toMatchObject({ credentials: 'include', cache: 'no-store' })
})

it('guides a customer with no past orders back to the shop', async () => {
  fetcher.mockImplementation(
    async (url) =>
      new Response(JSON.stringify(String(url).endsWith('/auth/session') ? customer : [])),
  )
  open()
  expect(await screen.findByRole('heading', { name: 'Your first order starts here.' })).toBeTruthy()
  expect(
    within(screen.getByRole('main'))
      .getByRole('link', { name: 'Explore the collection' })
      .getAttribute('href'),
  ).toBe('/shop')
})

it('lets customers retry a failed history request', async () => {
  let attempts = 0
  fetcher.mockImplementation(async (url) => {
    if (String(url).endsWith('/auth/session')) return new Response(JSON.stringify(customer))
    if (String(url).endsWith('/order/mine')) {
      attempts++
      return attempts === 1
        ? new Response(null, { status: 500 })
        : new Response(JSON.stringify(orders))
    }
    return new Response('[]')
  })
  const user = userEvent.setup()
  open()
  expect(await screen.findByRole('alert')).toBeTruthy()
  await user.click(screen.getByRole('button', { name: 'Try again' }))
  expect(await screen.findByRole('heading', { name: 'Order #42' })).toBeTruthy()
})

it('asks the customer to log in again if their session expires', async () => {
  fetcher.mockImplementation(async (url) => {
    if (String(url).endsWith('/auth/session')) return new Response(JSON.stringify(customer))
    if (String(url).endsWith('/order/mine')) return new Response(null, { status: 401 })
    return new Response('[]')
  })
  open()
  expect(await screen.findByRole('alert')).toHaveProperty(
    'textContent',
    'Log in again to see your orders.',
  )
  expect(
    within(screen.getByRole('main')).getByRole('link', { name: 'Log in' }).getAttribute('href'),
  ).toBe('/login?next=/my-orders')
})

it('requires login before requesting history', async () => {
  fetcher.mockImplementation(async (url) =>
    String(url).endsWith('/auth/session')
      ? new Response(null, { status: 401 })
      : new Response('[]'),
  )
  open()
  expect(await screen.findByLabelText('Email address')).toBeTruthy()
  expect(fetcher.mock.calls.some(([url]) => String(url).endsWith('/order/mine'))).toBe(false)
})
