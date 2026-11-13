import { savedBag, withCartServer } from './test/cartServer'
import { afterEach, beforeEach, expect, it, vi } from 'vitest'
import { cleanup, render, screen, waitFor, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

const userAccount = {
  userId: 9,
  firstName: 'Sam',
  lastName: 'Mokoena',
  email: 'sam@example.com',
  phoneNumber: '0712345678',
  role: 'CUSTOMER',
}
const fetcher = vi.fn<typeof fetch>()
let signedIn = false
function open(path = '/login') {
  window.history.replaceState({}, '', path)
  return render(<App />)
}
beforeEach(() => {
  signedIn = false
  localStorage.clear()
  sessionStorage.clear()
  vi.stubGlobal('scrollTo', vi.fn())
  fetcher.mockReset().mockImplementation(async (url, init) => {
    const path = String(url)
    if (path.endsWith('/auth/session'))
      return signedIn
        ? new Response(JSON.stringify(userAccount))
        : new Response(null, { status: 401 })
    if (path.endsWith('/auth/login')) {
      const input = JSON.parse(String(init?.body))
      if (input.email !== 'sam@example.com' || input.password !== 'SchoolLogin!2026')
        return new Response('Invalid email or password', { status: 401 })
      signedIn = true
      return new Response(JSON.stringify(userAccount))
    }
    if (path.endsWith('/auth/logout')) {
      signedIn = false
      return new Response(null, { status: 204 })
    }
    return new Response(
      JSON.stringify([{ productId: 1, name: 'Daily Cream', price: 245, stockQuantity: 2 }]),
    )
  })
  vi.stubGlobal('fetch', withCartServer(fetcher))
})
afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})

async function fillLogin(user: ReturnType<typeof userEvent.setup>, password = 'SchoolLogin!2026') {
  await user.type(await screen.findByLabelText('Email address'), 'Sam@Example.com')
  await user.type(screen.getByLabelText('Password', { exact: true }), password)
  await user.click(screen.getByRole('button', { name: 'Log in' }))
}

it('logs in an existing account, restores the session on reload, and logs out', async () => {
  const user = userEvent.setup()
  open()
  await fillLogin(user)
  expect(await screen.findByRole('heading', { name: /A daily ritual/ })).toBeTruthy()
  expect(window.location.pathname).toBe('/')
  expect(screen.getByRole('link', { name: 'My account' })).toBeTruthy()
  const call = fetcher.mock.calls.find(([url]) => String(url).endsWith('/auth/login'))
  expect(call?.[1]?.credentials).toBe('include')
  expect(JSON.parse(String(call?.[1]?.body))).toEqual({
    email: 'sam@example.com',
    password: 'SchoolLogin!2026',
  })
  expect(JSON.stringify(localStorage)).not.toContain('SchoolLogin')
  expect(JSON.stringify(sessionStorage)).not.toContain('sam@example.com')
  cleanup()
  open('/account')
  expect(await screen.findByRole('heading', { name: 'Hello, Sam.' })).toBeTruthy()
  await user.click(within(screen.getByRole('main')).getByRole('button', { name: 'Log out' }))
  expect(await screen.findByRole('heading', { name: 'Welcome back.' })).toBeTruthy()
  expect(signedIn).toBe(false)
  cleanup()
  open('/account')
  expect(await screen.findByRole('heading', { name: 'Welcome back.' })).toBeTruthy()
})

it('rejects an incorrect password and clears the password field', async () => {
  const user = userEvent.setup()
  open()
  await fillLogin(user, 'WrongPassword')
  expect((await screen.findByRole('alert')).textContent).toContain('email or password is incorrect')
  expect((screen.getByLabelText('Password', { exact: true }) as HTMLInputElement).value).toBe('')
  expect(screen.queryByRole('link', { name: 'My account' })).toBeNull()
})

it('shows a recoverable login connection error without signing in', async () => {
  const original = fetcher.getMockImplementation()!
  fetcher.mockImplementation((url, init) =>
    String(url).endsWith('/auth/login')
      ? Promise.reject(new TypeError('Offline'))
      : original(url, init),
  )
  const user = userEvent.setup()
  open()
  await fillLogin(user)
  expect((await screen.findByRole('alert')).textContent).toContain('reach the store')
  expect(signedIn).toBe(false)
})

it('ignores a forged browser account and requires a server session', async () => {
  localStorage.setItem('customer', JSON.stringify(userAccount))
  open('/account')
  expect(await screen.findByRole('heading', { name: 'Welcome back.' })).toBeTruthy()
  expect(screen.queryByRole('heading', { name: 'Hello, Sam.' })).toBeNull()
})

it('opens home after login from checkout, keeps the bag and still prefills checkout', async () => {
  localStorage.setItem('radiantskin.bag.v1', JSON.stringify([{ productId: 1, quantity: 1 }]))
  const user = userEvent.setup()
  open('/login?next=/checkout')
  await fillLogin(user)
  expect(await screen.findByRole('heading', { name: /A daily ritual/ })).toBeTruthy()
  expect(window.location.pathname).toBe('/')
  await user.click(screen.getByRole('link', { name: 'Shopping bag, 1 items' }))
  await user.click(await screen.findByRole('link', { name: 'Review checkout' }))
  await waitFor(() =>
    expect((screen.getByLabelText('First name') as HTMLInputElement).value).toBe('Sam'),
  )
  expect((screen.getByLabelText('Email address') as HTMLInputElement).value).toBe('sam@example.com')
  expect((screen.getByLabelText('Phone number') as HTMLInputElement).value).toBe('0712345678')
  expect(await savedBag()).toEqual([{ productId: 1, quantity: 1 }])
})

it('allows access to login from the mobile menu', async () => {
  const user = userEvent.setup()
  open('/shop')
  await user.click(screen.getByRole('button', { name: 'Open menu' }))
  const menu = screen.getByRole('navigation', { name: 'Mobile navigation' })
  await user.click(within(menu).getByRole('link', { name: 'Log in' }))
  expect(await screen.findByRole('heading', { name: 'Welcome back.' })).toBeTruthy()
})
