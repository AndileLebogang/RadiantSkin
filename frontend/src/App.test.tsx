import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { cleanup, render, screen, waitFor, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

const products = [
  {
    productId: 1,
    name: 'Daily Cream',
    brand: 'RadiantSkin',
    price: 245,
    stockQuantity: 2,
    usageInstructions: 'Apply as needed',
    description: 'Everyday care',
    reviews: [],
  },
  {
    productId: 2,
    name: 'Body Lotion',
    brand: 'RadiantSkin',
    price: 189,
    stockQuantity: 0,
    skinConcern: 'Daily care',
    reviews: [],
  },
  {
    productId: 3,
    name: 'Ritual Shampoo',
    brand: 'RadiantSkin',
    price: 210,
    stockQuantity: 5,
    hairConcern: 'Daily care',
    reviews: [],
  },
]

const receipt = {
  orderId: 42,
  orderDate: '2026-10-25T12:00:00',
  status: 'PROCESSING',
  totalAmount: 245,
  orderItems: [{ orderItemId: 1, quantity: 1, unitPrice: 245, product: { name: 'Daily Cream' } }],
}
const customer = {
  userId: 9,
  firstName: 'Sam',
  lastName: 'Mokoena',
  email: 'customer@example.com',
  phoneNumber: '0712345678',
  role: 'CUSTOMER',
}
async function fillCheckout(user: ReturnType<typeof userEvent.setup>) {
  await screen.findByLabelText('First name')
  for (const [label, value] of Object.entries({
    'First name': 'Sam',
    'Last name': 'Mokoena',
    'Email address': 'customer@example.com',
    'Phone number': '0712345678',
    'Street address': '1 Main Street',
    City: 'Pretoria',
    'Postal code': '0001',
  }))
    await user.type(screen.getByLabelText(label), value)
  await user.selectOptions(screen.getByLabelText('Province'), 'Gauteng')
  await user.click(screen.getByRole('button', { name: 'Review details' }))
  await screen.findByRole('heading', { name: 'Your checkout details' })
}
async function fillCard(user: ReturnType<typeof userEvent.setup>, card: string) {
  await user.type(screen.getByLabelText('Name on card'), 'Sam Mokoena')
  await user.type(screen.getByLabelText('Card number'), card)
  await user.type(screen.getByLabelText('Expiry date (MM/YY)'), '12/30')
  await user.type(screen.getByLabelText('Security code'), '9876')
}

const fetcher = vi.fn<typeof fetch>()
function open(path = '/shop') {
  window.history.replaceState({}, '', path)
  return render(<App />)
}
beforeEach(() => {
  localStorage.clear()
  fetcher.mockReset().mockImplementation(
    async (url) =>
      new Response(JSON.stringify(String(url).includes('/review/product/') ? [] : products), {
        status: 200,
      }),
  )
  vi.stubGlobal('fetch', fetcher)
  vi.stubGlobal('scrollTo', vi.fn())
})
afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})

describe('Customer storefront', () => {
  it('filters the catalogue by category, search, and availability', async () => {
    const user = userEvent.setup()
    open()
    await screen.findByRole('link', { name: 'Daily Cream' })
    await user.click(screen.getByRole('button', { name: 'Hair care' }))
    expect(screen.queryByRole('link', { name: 'Daily Cream' })).toBeNull()
    expect(screen.getByRole('link', { name: 'Ritual Shampoo' })).toBeTruthy()
    await user.click(screen.getByRole('button', { name: 'Shop all' }))
    await user.click(screen.getByLabelText('In stock only'))
    expect(screen.queryByRole('link', { name: 'Body Lotion' })).toBeNull()
    await user.type(screen.getByRole('searchbox', { name: 'Search products' }), 'cream')
    expect(screen.queryByRole('link', { name: 'Ritual Shampoo' })).toBeNull()
    expect(screen.getByRole('link', { name: 'Daily Cream' })).toBeTruthy()
  })
  it('adds to the bag, enforces stock limits, persists, and removes an item', async () => {
    const user = userEvent.setup()
    open()
    const add = await screen.findByRole('button', { name: 'Add Daily Cream to bag' })
    await user.click(add)
    await user.click(add)
    expect((add as HTMLButtonElement).disabled).toBe(true)
    await waitFor(() =>
      expect(JSON.parse(localStorage.getItem('radiantskin.bag.v1') || '[]')).toEqual([
        { productId: 1, quantity: 2 },
      ]),
    )
    await user.click(screen.getByRole('link', { name: 'Shopping bag, 2 items' }))
    expect(screen.getByRole('heading', { name: 'Daily Cream' })).toBeTruthy()
    await user.click(screen.getByRole('button', { name: 'Remove Daily Cream from bag' }))
    expect(
      screen.getByRole('heading', { name: 'Your bag is waiting for a little care' }),
    ).toBeTruthy()
  })
  it('shows a recoverable error then reloads the real catalogue', async () => {
    fetcher.mockRejectedValueOnce(new TypeError('Network failure'))
    const user = userEvent.setup()
    open()
    expect(await screen.findByRole('alert')).toBeTruthy()
    await user.click(screen.getByRole('button', { name: 'Try again' }))
    expect(await screen.findByRole('link', { name: 'Daily Cream' })).toBeTruthy()
  })
  it('keeps unavailable saved items visible and blocks checkout until removed', async () => {
    localStorage.setItem('radiantskin.bag.v1', JSON.stringify([{ productId: 999, quantity: 1 }]))
    open('/bag')
    expect(await screen.findByRole('heading', { name: 'Product no longer available' })).toBeTruthy()
    expect(screen.queryByRole('link', { name: 'Review checkout' })).toBeNull()
  })
  it('renders product details and a real empty catalogue without invented products', async () => {
    open('/products/1')
    expect(await screen.findByRole('heading', { level: 1, name: 'Daily Cream' })).toBeTruthy()
    expect(screen.getByText('Apply as needed')).toBeTruthy()
    cleanup()
    fetcher.mockResolvedValueOnce(new Response('[]'))
    open('/shop')
    expect(
      await screen.findByRole('heading', { name: 'Our shelves are being prepared' }),
    ).toBeTruthy()
  })
  it('loads product reviews and lets a registered customer post one', async () => {
    fetcher.mockImplementation(async (url) => {
      const path = String(url)
      if (path.endsWith('/auth/session')) return new Response(JSON.stringify(customer))
      if (path.endsWith('/review/submit'))
        return new Response(
          JSON.stringify({
            reviewId: 12,
            rating: 5,
            comment: 'Lovely texture',
            reviewDate: '2026-10-25',
          }),
          { status: 201 },
        )
      if (path.includes('/review/product/'))
        return new Response(
          JSON.stringify([
            { reviewId: 11, rating: 4, comment: 'Gentle on my skin', reviewDate: '2026-10-24' },
          ]),
        )
      return new Response(JSON.stringify(products))
    })
    const user = userEvent.setup()
    open('/products/1')
    expect(await screen.findByText('Gentle on my skin')).toBeTruthy()
    await user.selectOptions(screen.getByLabelText('Rating'), '5')
    await user.type(screen.getByLabelText('Your review'), 'Lovely texture')
    expect(await screen.findByText('Reviewing as Sam.')).toBeTruthy()
    expect(screen.queryByLabelText('Account email')).toBeNull()
    expect(screen.queryByLabelText('Account password')).toBeNull()
    await user.click(screen.getByRole('button', { name: 'Post review' }))
    expect(await screen.findByText('Your review has been posted.')).toBeTruthy()
    expect(screen.getByText('Lovely texture')).toBeTruthy()
    const call = fetcher.mock.calls.find(([url]) => String(url).endsWith('/review/submit'))
    expect(call?.[1]?.method).toBe('POST')
    expect(call?.[1]?.credentials).toBe('include')
    expect(JSON.parse(String(call?.[1]?.body))).toEqual({
      productId: 1,
      rating: 5,
      comment: 'Lovely texture',
    })
  })
  it('handles an expired review session without adding a review', async () => {
    let expired = false
    fetcher.mockImplementation(async (url) => {
      const path = String(url)
      if (path.endsWith('/auth/session'))
        return expired ? new Response('', { status: 401 }) : new Response(JSON.stringify(customer))
      if (path.endsWith('/review/submit')) {
        expired = true
        return new Response('Log in to post your review.', { status: 401 })
      }
      return new Response(JSON.stringify(path.includes('/review/product/') ? [] : products))
    })
    const user = userEvent.setup()
    open('/products/1')
    await screen.findByText('No reviews yet. Be the first to share your experience.')
    await user.selectOptions(screen.getByLabelText('Rating'), '4')
    await user.type(screen.getByLabelText('Your review'), 'Feels good')
    await user.click(screen.getByRole('button', { name: 'Post review' }))
    expect((await screen.findByRole('alert')).textContent).toContain('Your session has expired')
    expect(screen.queryByText('Feels good')).toBeNull()
    expect(await screen.findByRole('link', { name: 'Log in to write a review' })).toBeTruthy()
    expect(screen.queryByRole('button', { name: 'Post review' })).toBeNull()
  })
  it('lets guests read reviews and returns them to the product after login', async () => {
    fetcher.mockImplementation(async (url) => {
      const path = String(url)
      if (path.endsWith('/auth/session')) return new Response('', { status: 401 })
      if (path.endsWith('/auth/login')) return new Response(JSON.stringify(customer))
      return new Response(JSON.stringify(path.includes('/review/product/') ? [] : products))
    })
    const user = userEvent.setup()
    open('/products/1')
    await user.click(await screen.findByRole('link', { name: 'Log in to write a review' }))
    await user.type(screen.getByLabelText('Email address'), customer.email)
    await user.type(screen.getByLabelText('Password'), 'SecretPassword123')
    await user.click(screen.getByRole('button', { name: 'Log in' }))
    expect(await screen.findByText('Reviewing as Sam.')).toBeTruthy()
    expect(window.location.pathname).toBe('/products/1')
    expect(screen.queryByLabelText('Account password')).toBeNull()
  })
  it('does not offer customer review submission to admins', async () => {
    fetcher.mockImplementation(
      async (url) =>
        new Response(
          JSON.stringify(
            String(url).endsWith('/auth/session')
              ? { ...customer, role: 'ADMIN' }
              : String(url).includes('/review/product/')
                ? []
                : products,
          ),
        ),
    )
    open('/products/1')
    expect(await screen.findByText('Only customer accounts can post reviews.')).toBeTruthy()
    expect(screen.queryByRole('button', { name: 'Post review' })).toBeNull()
  })
  it.each(['4111 1111 1111 1111', '0000 0000 0000 0002', '7'])(
    'places an order with card %s, clears the bag, and keeps card values private',
    async (card) => {
      fetcher.mockImplementation(
        async (url) =>
          new Response(
            JSON.stringify(String(url).endsWith('/checkout/place') ? receipt : products),
            { status: String(url).endsWith('/checkout/place') ? 201 : 200 },
          ),
      )
      localStorage.setItem('radiantskin.bag.v1', JSON.stringify([{ productId: 1, quantity: 1 }]))
      const user = userEvent.setup()
      open('/checkout')
      await fillCheckout(user)
      expect(document.body.textContent).not.toMatch(/demo|simulation|fictional|test card/i)
      await fillCard(user, card)
      await user.click(screen.getByRole('button', { name: 'Place order' }))
      expect(await screen.findByRole('heading', { name: 'Thank you for your order.' })).toBeTruthy()
      expect(screen.getByText('Your order has been placed and your bag is now empty.')).toBeTruthy()
      expect(screen.getByRole('link', { name: 'Shopping bag, 0 items' })).toBeTruthy()
      const calls = fetcher.mock.calls.filter(([url]) => String(url).endsWith('/checkout/place'))
      expect(calls).toHaveLength(1)
      const payload = JSON.parse(String(calls[0][1]?.body))
      expect(payload).toMatchObject({
        items: [{ productId: 1, quantity: 1 }],
        expectedTotal: 245,
        details: { email: 'customer@example.com', street: '1 Main Street' },
      })
      expect(Object.keys(payload).sort()).toEqual([
        'details',
        'expectedTotal',
        'items',
        'requestId',
      ])
      expect(String(calls[0][1]?.body)).not.toMatch(/cardNumber|securityCode|expiry|cardholder/)
      expect(JSON.stringify(localStorage)).not.toContain('customer@example.com')
      expect(JSON.stringify(localStorage)).not.toContain('9876')
      await waitFor(() => expect(localStorage.getItem('radiantskin.bag.v1')).toBe('[]'))
      await user.click(screen.getByRole('link', { name: 'Shopping bag, 0 items' }))
      expect(
        await screen.findByRole('heading', { name: 'Your bag is waiting for a little care' }),
      ).toBeTruthy()
      cleanup()
      open('/bag')
      expect(
        await screen.findByRole('heading', { name: 'Your bag is waiting for a little care' }),
      ).toBeTruthy()
    },
    15000,
  )
  it('preserves the bag on failure and retries with the same order request ID', async () => {
    let attempts = 0
    fetcher.mockImplementation(async (url) => {
      if (String(url).endsWith('/checkout/place')) {
        attempts++
        if (attempts === 1) throw new TypeError('Lost response')
        return new Response(JSON.stringify(receipt), { status: 201 })
      }
      return new Response(JSON.stringify(products))
    })
    localStorage.setItem('radiantskin.bag.v1', JSON.stringify([{ productId: 1, quantity: 1 }]))
    const user = userEvent.setup()
    open('/checkout')
    await fillCheckout(user)
    await fillCard(user, '1234')
    await user.click(screen.getByRole('button', { name: 'Place order' }))
    expect((await screen.findByRole('alert')).textContent).toContain('reach the store')
    expect(localStorage.getItem('radiantskin.bag.v1')).toContain('"productId":1')
    expect((screen.getByLabelText('Security code') as HTMLInputElement).value).toBe('')
    expect(screen.queryByRole('heading', { name: 'Thank you for your order.' })).toBeNull()
    await fillCard(user, '9999')
    await user.click(screen.getByRole('button', { name: 'Place order' }))
    await screen.findByRole('heading', { name: 'Thank you for your order.' })
    const calls = fetcher.mock.calls.filter(([url]) => String(url).endsWith('/checkout/place'))
    expect(JSON.parse(String(calls[0][1]?.body)).requestId).toBe(
      JSON.parse(String(calls[1][1]?.body)).requestId,
    )
    await waitFor(() => expect(localStorage.getItem('radiantskin.bag.v1')).toBe('[]'))
  }, 15000)
  it('shows stock conflicts without clearing the bag', async () => {
    fetcher.mockImplementation(async (url) =>
      String(url).endsWith('/checkout/place')
        ? new Response(
            JSON.stringify({ message: 'Some items no longer have enough stock. Review your bag.' }),
            { status: 409 },
          )
        : new Response(JSON.stringify(products)),
    )
    localStorage.setItem('radiantskin.bag.v1', JSON.stringify([{ productId: 1, quantity: 1 }]))
    const user = userEvent.setup()
    open('/checkout')
    await fillCheckout(user)
    await fillCard(user, '1234')
    await user.click(screen.getByRole('button', { name: 'Place order' }))
    expect((await screen.findByRole('alert')).textContent).toContain('enough stock')
    expect(localStorage.getItem('radiantskin.bag.v1')).toContain('"productId":1')
  }, 15000)
  it('looks up an existing order and displays its latest status', async () => {
    fetcher.mockImplementation(
      async (url) =>
        new Response(
          JSON.stringify(
            String(url).includes('/order/read/')
              ? {
                  orderId: 42,
                  orderDate: '2026-09-09T12:00:00',
                  status: 'SHIPPED',
                  totalAmount: 245,
                  orderItems: [
                    {
                      orderItemId: 1,
                      quantity: 1,
                      unitPrice: 245,
                      product: { name: 'Daily Cream' },
                    },
                  ],
                }
              : products,
          ),
        ),
    )
    const user = userEvent.setup()
    open('/orders')
    await user.type(screen.getByLabelText('Order number'), '42')
    await user.click(screen.getByRole('button', { name: 'Track order' }))
    expect(await screen.findByRole('heading', { name: 'shipped' })).toBeTruthy()
    expect(screen.getByText(/Keep your order number handy/)).toBeTruthy()
  })
  it('provides mobile menu navigation and a useful unknown-route page', async () => {
    const user = userEvent.setup()
    open('/missing')
    expect(screen.getByRole('heading', { name: 'A little off the beaten path' })).toBeTruthy()
    await user.click(screen.getByRole('button', { name: 'Open menu' }))
    await user.click(
      within(screen.getByRole('navigation', { name: 'Mobile navigation' })).getByRole('link', {
        name: 'Hair care',
      }),
    )
    expect(await screen.findByRole('link', { name: 'Ritual Shampoo' })).toBeTruthy()
    expect(screen.queryByRole('navigation', { name: 'Mobile navigation' })).toBeNull()
  })
})

async function fillRegistration(user: ReturnType<typeof userEvent.setup>) {
  await user.type(screen.getByLabelText('First name'), 'Sam')
  await user.type(screen.getByLabelText('Last name'), 'Mokoena')
  await user.type(screen.getByLabelText('Email address'), 'Sam@Example.com')
  await user.type(screen.getByLabelText('Phone number'), '0712345678')
  await user.type(screen.getByLabelText('Password'), 'StrongPass123')
  await user.type(screen.getByLabelText('Confirm password'), 'StrongPass123')
}

describe('Customer account creation', () => {
  it('opens from storefront navigation and creates an account through the API', async () => {
    fetcher.mockImplementation(async (url) =>
      String(url).endsWith('/customer/register')
        ? new Response('{}', { status: 201 })
        : new Response(JSON.stringify(products), { status: 200 }),
    )
    const user = userEvent.setup()
    open()
    await user.click(screen.getByRole('link', { name: 'Create account' }))
    expect(screen.getByRole('heading', { name: 'Create an account.' })).toBeTruthy()
    await fillRegistration(user)
    await user.click(screen.getByRole('button', { name: 'Create account' }))

    expect(
      await screen.findByRole('heading', { name: 'Your account has been created.' }),
    ).toBeTruthy()
    const call = fetcher.mock.calls.find(([url]) => String(url).endsWith('/customer/register'))
    expect(call?.[0]).toBe('/api/customer/register')
    expect(JSON.parse(String(call?.[1]?.body))).toEqual({
      firstName: 'Sam',
      lastName: 'Mokoena',
      email: 'sam@example.com',
      phoneNumber: '0712345678',
      password: 'StrongPass123',
    })
    expect(localStorage.getItem('customer')).toBeNull()
  })

  it('keeps the form open and explains a duplicate email', async () => {
    fetcher.mockImplementation(async (url) =>
      String(url).endsWith('/customer/register')
        ? new Response('This email is already registered', { status: 400 })
        : new Response(JSON.stringify(products), { status: 200 }),
    )
    const user = userEvent.setup()
    open('/register')
    await fillRegistration(user)
    await user.click(screen.getByRole('button', { name: 'Create account' }))

    expect((await screen.findByRole('alert')).textContent).toContain(
      'An account with this email already exists.',
    )
    expect((screen.getByLabelText('Email address') as HTMLInputElement).value).toBe(
      'Sam@Example.com',
    )
  })
})
