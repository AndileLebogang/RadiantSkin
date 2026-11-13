import { describe, expect, it } from 'vitest'
import { transferReference, validateCard } from './payment'

const now = new Date(2026, 10, 13)
const card = {
  cardholder: 'Sam Mokoena',
  cardNumber: '4111 1111 1111 1111',
  expiry: '11/26',
  securityCode: '123',
}
describe('Card validation', () => {
  it.each(['', '7', '1234', '4111111111111112', '0000000000000000', '4111AB1111111111'])(
    'accepts card number input without validation: %s',
    (cardNumber) => {
      expect(validateCard({ ...card, cardNumber }, now)).toEqual({})
    },
  )
  it('accepts the current expiry month but rejects expired cards and invalid months', () => {
    expect(validateCard(card, now).expiry).toBeUndefined()
    for (const expiry of ['10/26', '12/25', '00/30', '13/30', '1230']) {
      expect(validateCard({ ...card, expiry }, now).expiry).toBeTruthy()
    }
  })
  it('requires a cardholder and security code', () => {
    const errors = validateCard({ ...card, cardholder: ' ', securityCode: '12' }, now)
    expect(errors.cardholder).toBeTruthy()
    expect(errors.securityCode).toBeTruthy()
  })
})
it('keeps the EFT reference stable across retries', () => {
  expect(transferReference('12345678-9abc-4def-abcd-123456789abc')).toBe('RS-123456789ABC')
})
