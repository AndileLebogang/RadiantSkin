import { afterEach, beforeEach, expect, it, vi } from 'vitest'
import { cleanup, render, screen, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

const admin = {
  userId: 17,
  firstName: 'Sam',
  lastName: 'Mokoena',
  email: 'staff@example.test',
  phoneNumber: '',
  role: 'ADMIN',
}
const fetcher = vi.fn<typeof fetch>()
beforeEach(() => {
  localStorage.clear()
  sessionStorage.clear()
  vi.stubGlobal('scrollTo', vi.fn())
  fetcher.mockReset().mockImplementation(async (url) => {
    const path = String(url)
    if (path.endsWith('/auth/session')) return new Response(null, { status: 401 })
    if (path.endsWith('/admin/register'))
      return new Response(JSON.stringify(admin), { status: 201 })
    if (path.endsWith('/auth/login')) return new Response(JSON.stringify(admin))
    return new Response('[]')
  })
  vi.stubGlobal('fetch', fetcher)
})
afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})
function open(path = '/admin/register') {
  window.history.replaceState({}, '', path)
  render(<App />)
}
async function fill(user: ReturnType<typeof userEvent.setup>) {
  for (const [label, value] of Object.entries({
    'First name': 'Sam',
    'Last name': 'Mokoena',
    'Employee number': 'EMP-17',
    'Email address': 'Staff@Example.test',
    Password: 'SchoolStaff123',
    'Confirm password': 'SchoolStaff123',
  }))
    await user.type(await screen.findByLabelText(label), value)
}
it('opens public admin registration from login, registers and logs into the admin portal', async () => {
  const user = userEvent.setup()
  open('/login')
  await user.click(await screen.findByRole('link', { name: 'Register as admin' }))
  expect(await screen.findByRole('heading', { name: 'Create an admin account.' })).toBeTruthy()
  expect(screen.queryByLabelText('Phone number')).toBeNull()
  await fill(user)
  await user.click(screen.getByRole('button', { name: 'Create admin account' }))
  expect(
    await screen.findByRole('heading', { name: 'Your admin account has been created.' }),
  ).toBeTruthy()
  const registration = fetcher.mock.calls.find(([url]) => String(url).endsWith('/admin/register'))
  expect(JSON.parse(String(registration?.[1]?.body))).toEqual({
    firstName: 'Sam',
    lastName: 'Mokoena',
    employeeNumber: 'EMP-17',
    email: 'staff@example.test',
    password: 'SchoolStaff123',
  })
  await user.click(
    within(
      screen.getByRole('heading', { name: 'Your admin account has been created.' }).parentElement!,
    ).getByRole('link', { name: 'Log in' }),
  )
  await user.type(await screen.findByLabelText('Email address'), 'staff@example.test')
  await user.type(screen.getByLabelText('Password'), 'SchoolStaff123')
  await user.click(screen.getByRole('button', { name: 'Log in' }))
  expect(await screen.findByRole('heading', { name: 'Admin portal.' })).toBeTruthy()
  expect(screen.getByRole('button', { name: 'Products' })).toBeTruthy()
  expect(window.location.pathname).toBe('/admin')
  expect(JSON.stringify(localStorage)).not.toContain('SchoolStaff123')
  expect(JSON.stringify(sessionStorage)).not.toContain('SchoolStaff123')
})
it('validates employee number and matching passwords before sending registration', async () => {
  const user = userEvent.setup()
  open()
  await fill(user)
  await user.clear(screen.getByLabelText('Employee number'))
  await user.clear(screen.getByLabelText('Confirm password'))
  await user.type(screen.getByLabelText('Confirm password'), 'different')
  await user.click(screen.getByRole('button', { name: 'Create admin account' }))
  expect(screen.getByText('Enter your employee number.')).toBeTruthy()
  expect(screen.getByText('The passwords do not match.')).toBeTruthy()
  expect(fetcher.mock.calls.some(([url]) => String(url).endsWith('/admin/register'))).toBe(false)
})
it('explains a duplicate email and keeps the admin form available', async () => {
  const original = fetcher.getMockImplementation()!
  fetcher.mockImplementation((url, init) =>
    String(url).endsWith('/admin/register')
      ? Promise.resolve(new Response('This email is already registered', { status: 400 }))
      : original(url, init),
  )
  const user = userEvent.setup()
  open()
  await fill(user)
  await user.click(screen.getByRole('button', { name: 'Create admin account' }))
  expect((await screen.findByRole('alert')).textContent).toContain(
    'An account with this email already exists.',
  )
  expect((screen.getByLabelText('Email address') as HTMLInputElement).value).toBe(
    'Staff@Example.test',
  )
  expect(screen.queryByRole('heading', { name: 'Your admin account has been created.' })).toBeNull()
})
