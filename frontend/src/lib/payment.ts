export type CardField = 'cardholder' | 'cardNumber' | 'expiry' | 'securityCode'
export type CardDetails = Record<CardField, string>
export const emptyCard: CardDetails = {
  cardholder: '',
  cardNumber: '',
  expiry: '',
  securityCode: '',
}
export const cardFields: CardField[] = ['cardholder', 'cardNumber', 'expiry', 'securityCode']

export function validateCard(
  details: CardDetails,
  now = new Date(),
): Partial<Record<CardField, string>> {
  const errors: Partial<Record<CardField, string>> = {}
  if (!details.cardholder.trim()) errors.cardholder = 'Enter the name on your card.'
  if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(details.expiry)) {
    errors.expiry = 'Enter the expiry date as MM/YY.'
  } else {
    const [month, year] = details.expiry.split('/').map(Number)
    if (
      2000 + year < now.getFullYear() ||
      (2000 + year === now.getFullYear() && month < now.getMonth() + 1)
    )
      errors.expiry = 'This card has expired. Use another card.'
  }
  if (!/^\d{3,4}$/.test(details.securityCode))
    errors.securityCode = 'Enter the 3 or 4 digit security code.'
  return errors
}

export function transferReference(requestId: string) {
  return `RS-${requestId.replaceAll('-', '').slice(0, 12).toUpperCase()}`
}
