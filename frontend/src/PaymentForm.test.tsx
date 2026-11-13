import { afterEach, expect, it, vi } from 'vitest'
import { cleanup, render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import PaymentForm from './PaymentForm'
import type { PaymentMethod } from './lib/store'

afterEach(() => {
  cleanup()
  vi.restoreAllMocks()
})
function open(
  onPlaceOrder: (method: PaymentMethod) => Promise<void> = vi.fn().mockResolvedValue(undefined),
) {
  render(
    <PaymentForm
      total={245}
      requestId="12345678-9abc-4def-abcd-123456789abc"
      onPlaceOrder={onPlaceOrder}
      onProcessingChange={vi.fn()}
    />,
  )
  return onPlaceOrder
}
it('accepts an unchecked card number while requiring the other card details', async () => {
  const user = userEvent.setup()
  const place = open()
  await user.click(screen.getByRole('button', { name: 'Place order' }))
  expect(document.activeElement).toBe(screen.getByLabelText('Name on card'))
  expect(place).not.toHaveBeenCalled()
  await user.type(screen.getByLabelText('Name on card'), 'Sam')
  await user.type(screen.getByLabelText('Card number'), '1234')
  await user.type(screen.getByLabelText('Expiry date (MM/YY)'), '0130')
  await user.type(screen.getByLabelText('Security code'), '123')
  expect((screen.getByLabelText('Expiry date (MM/YY)') as HTMLInputElement).value).toBe('01/30')
  await user.click(screen.getByRole('button', { name: 'Place order' }))
  expect(place).toHaveBeenCalledExactlyOnceWith('CARD')
})
it('shows copyable EFT details and requires transfer confirmation', async () => {
  const user = userEvent.setup()
  const place = open()
  await user.click(screen.getByRole('radio', { name: 'EFT / Bank transfer' }))
  expect(screen.getByText('RS-123456789ABC')).toBeTruthy()
  await user.click(screen.getByRole('button', { name: 'Copy payment reference' }))
  expect(await navigator.clipboard.readText()).toBe('RS-123456789ABC')
  await user.click(screen.getByRole('button', { name: 'Place order' }))
  expect(screen.getByText('Confirm the transfer before placing your order.')).toBeTruthy()
  expect(place).not.toHaveBeenCalled()
  await user.click(screen.getByLabelText('I have completed the transfer.'))
  await user.click(screen.getByRole('button', { name: 'Place order' }))
  expect(place).toHaveBeenCalledExactlyOnceWith('EFT')
})
it('reviews PayPal details before approval and permits cancellation without creating an order', async () => {
  const user = userEvent.setup()
  const place = open()
  await user.click(screen.getByRole('radio', { name: 'PayPal' }))
  await user.click(screen.getByRole('button', { name: 'Review payment' }))
  expect(screen.getByText('Enter a valid PayPal email address.')).toBeTruthy()
  await user.type(screen.getByLabelText('PayPal email address'), 'payer@example.test')
  await user.click(screen.getByRole('button', { name: 'Review payment' }))
  expect(await screen.findByRole('heading', { name: 'Review your payment' })).toBeTruthy()
  expect(place).not.toHaveBeenCalled()
  expect(screen.queryByLabelText(/password/i)).toBeNull()
  await user.click(screen.getByRole('button', { name: 'Change PayPal account' }))
  expect(screen.getByLabelText('PayPal email address')).toBeTruthy()
  await user.click(screen.getByRole('button', { name: 'Review payment' }))
  await user.click(screen.getByRole('button', { name: 'Approve & place order' }))
  expect(place).toHaveBeenCalledExactlyOnceWith('PAYPAL')
})
it('prevents duplicate submissions during payment processing', async () => {
  let finish!: () => void
  const place = vi.fn(
    () =>
      new Promise<void>((resolve) => {
        finish = resolve
      }),
  )
  const user = userEvent.setup()
  open(place)
  await user.click(screen.getByRole('radio', { name: 'EFT / Bank transfer' }))
  await user.click(screen.getByLabelText('I have completed the transfer.'))
  await user.click(screen.getByRole('button', { name: 'Place order' }))
  const button = screen.getByRole('button', { name: 'Confirming your order…' }) as HTMLButtonElement
  expect(button.disabled).toBe(true)
  await user.click(button)
  expect(place).toHaveBeenCalledTimes(1)
  finish()
  await waitFor(() => expect(screen.getByRole('button', { name: 'Place order' })).toBeTruthy())
})
