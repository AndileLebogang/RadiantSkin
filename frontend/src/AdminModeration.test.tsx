import { afterEach, expect, it, vi } from 'vitest'
import { act, cleanup, render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'
import { withCartServer } from './test/cartServer'

afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})
it('keeps the selected moderation results when an older request finishes later', async () => {
  let hidden = false
  let approvedRequests = 0
  let finishOldRequest: ((response: Response) => void) | undefined
  const review = {
    reviewId: 1,
    productId: 2,
    rating: 5,
    comment: 'Saved customer review',
    reviewDate: '2026-11-13',
  }
  const page = (content: unknown[]) =>
    new Response(
      JSON.stringify({
        content,
        page: 0,
        size: 10,
        totalElements: content.length,
        totalPages: content.length ? 1 : 0,
        last: true,
      }),
    )
  const fetcher: typeof fetch = async (url, init) => {
    const path = String(url)
    if (path.endsWith('/auth/session'))
      return new Response(
        JSON.stringify({
          userId: 3,
          firstName: 'Sam',
          lastName: 'Staff',
          email: 'staff@example.test',
          phoneNumber: '',
          role: 'ADMIN',
        }),
      )
    if (path.endsWith('/review/admin/1/status') && init?.method === 'PUT') {
      hidden = true
      return new Response(JSON.stringify(review))
    }
    if (path.includes('/review/admin/moderation?status=APPROVED')) {
      approvedRequests++
      if (approvedRequests > 1)
        return new Promise<Response>((resolve) => {
          finishOldRequest = resolve
        })
      return page([review])
    }
    if (path.includes('/review/admin/moderation?status=HIDDEN')) return page(hidden ? [review] : [])
    return new Response('[]')
  }
  vi.stubGlobal('fetch', withCartServer(fetcher))
  vi.stubGlobal('scrollTo', vi.fn())
  localStorage.clear()
  window.history.replaceState({}, '', '/admin')
  const user = userEvent.setup()
  render(<App />)
  await user.click(await screen.findByRole('button', { name: 'Reviews' }))
  await user.click(screen.getByRole('button', { name: 'Approved' }))
  await screen.findByText('Saved customer review')
  await user.click(screen.getByRole('button', { name: 'Hide review' }))
  await waitFor(() => expect(finishOldRequest).toBeTypeOf('function'))
  await user.click(screen.getByRole('button', { name: 'Hidden' }))
  await screen.findByText('Saved customer review')
  await act(async () => { finishOldRequest!(page([])) })
  await waitFor(() => expect(screen.getByRole('button', { name: 'Approve review' })).toBeTruthy())
  expect(screen.getByText('Saved customer review')).toBeTruthy()
  expect(screen.queryByRole('heading', { name: 'No hidden reviews' })).toBeNull()
})
