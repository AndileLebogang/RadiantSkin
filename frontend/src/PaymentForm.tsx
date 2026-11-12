import { useEffect, useRef, useState } from 'react'
import type { ChangeEvent, FormEvent } from 'react'
import { paymentMethodLabels } from './lib/store'
import type { PaymentMethod } from './lib/store'

type Field = 'cardholder' | 'cardNumber' | 'expiry' | 'securityCode'
type Details = Record<Field, string>
const empty: Details = { cardholder: '', cardNumber: '', expiry: '', securityCode: '' }
const fields: Field[] = ['cardholder', 'cardNumber', 'expiry', 'securityCode']

function validate(details: Details): Partial<Record<Field, string>> {
  const errors: Partial<Record<Field, string>> = {}
  if (!details.cardholder.trim()) errors.cardholder = 'Enter the name on your card.'
  if (!/^\d+$/.test(details.cardNumber.replace(/[\s-]/g, '')))
    errors.cardNumber = 'Enter a card number using digits.'
  if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(details.expiry.trim()))
    errors.expiry = 'Enter the expiry date as MM/YY.'
  if (!/^\d{3,4}$/.test(details.securityCode.trim()))
    errors.securityCode = 'Enter a 3 or 4 digit security code.'
  return errors
}

export default function PaymentForm({
  onPlaceOrder,
  onProcessingChange,
}: {
  onPlaceOrder: (method: PaymentMethod) => Promise<void>
  onProcessingChange: (processing: boolean) => void
}) {
  const [details, setDetails] = useState<Details>(empty)
  const [method, setMethod] = useState<PaymentMethod>('CARD')
  const [methodLocked, setMethodLocked] = useState(false)
  const [touched, setTouched] = useState<Partial<Record<Field, boolean>>>({})
  const [attempted, setAttempted] = useState(false)
  const [processing, setProcessing] = useState(false)
  const [serverError, setServerError] = useState('')
  const submitting = useRef(false)
  const errorRef = useRef<HTMLDivElement>(null)
  const errors = validate(details)

  useEffect(() => {
    if (serverError) errorRef.current?.focus()
  }, [serverError])

  function change(event: ChangeEvent<HTMLInputElement>) {
    const field = event.currentTarget.name as Field
    const value = event.currentTarget.value
    setDetails((current) => ({ ...current, [field]: value }))
    setServerError('')
  }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (submitting.current) return
    setAttempted(true)
    setServerError('')
    const firstError = method === 'CARD' && fields.find((field) => errors[field])
    if (firstError) {
      document.getElementById(firstError)?.focus()
      return
    }
    submitting.current = true
    setMethodLocked(true)
    setProcessing(true)
    onProcessingChange(true)
    try {
      await onPlaceOrder(method)
    } catch (error) {
      setServerError(
        error instanceof Error ? error.message : 'We could not place your order. Please try again.',
      )
    } finally {
      // Clear card values after every request, including a failed checkout.
      setDetails(empty)
      setTouched({})
      setAttempted(false)
      submitting.current = false
      setProcessing(false)
      onProcessingChange(false)
    }
  }

  return (
    <section className="card-payment" aria-labelledby="payment-heading">
      <span className="eyebrow">03 / Payment</span>
      <h3 id="payment-heading">Payment method</h3>
      <p className="card-payment-intro">
        Choose how you would like to pay. Online payments are not connected yet. Your order will be
        saved as awaiting payment; no payment will be taken here.
      </p>
      {serverError && (
        <div className="account-alert" role="alert" tabIndex={-1} ref={errorRef}>
          {serverError}
        </div>
      )}
      <form onSubmit={submit} autoComplete="off" noValidate>
        <fieldset className="payment-methods" disabled={processing || methodLocked}>
          <legend className="sr-only">Choose a payment method</legend>
          {(Object.keys(paymentMethodLabels) as PaymentMethod[]).map((value) => (
            <label className={`payment-option ${method === value ? 'selected' : ''}`} key={value}>
              <input
                type="radio"
                name="paymentMethod"
                value={value}
                checked={method === value}
                onChange={() => {
                  setMethod(value)
                  setDetails(empty)
                  setTouched({})
                  setAttempted(false)
                  setServerError('')
                }}
              />
              <span>{paymentMethodLabels[value]}</span>
            </label>
          ))}
        </fieldset>
        {methodLocked && serverError && (
          <p className="form-note">
            Retry with the same payment method to avoid a duplicate order.
          </p>
        )}
        {method === 'CARD' ? (
          <>
            <h3>Card details</h3>
            <div className="card-payment-fields">
              {(
                [
                  ['cardholder', 'Name on card', 'text', 'text'],
                  ['cardNumber', 'Card number', 'text', 'numeric'],
                  ['expiry', 'Expiry date (MM/YY)', 'text', 'numeric'],
                  ['securityCode', 'Security code', 'password', 'numeric'],
                ] as const
              ).map(([field, label, type, inputMode]) => {
                const error = (touched[field] || attempted) && errors[field]
                return (
                  <div
                    className={`card-payment-field ${field === 'cardholder' || field === 'cardNumber' ? 'wide' : ''}`}
                    key={field}
                  >
                    <label htmlFor={field}>{label}</label>
                    <input
                      id={field}
                      name={field}
                      type={type}
                      inputMode={inputMode}
                      autoComplete="off"
                      value={details[field]}
                      onChange={change}
                      disabled={processing}
                      onBlur={() => setTouched((current) => ({ ...current, [field]: true }))}
                      aria-invalid={Boolean(error)}
                      aria-describedby={error ? `${field}-error` : undefined}
                      maxLength={
                        field === 'cardNumber'
                          ? 32
                          : field === 'expiry'
                            ? 5
                            : field === 'securityCode'
                              ? 4
                              : 80
                      }
                      placeholder={field === 'expiry' ? 'MM/YY' : undefined}
                      required
                    />
                    {error && (
                      <span className="account-field-error" id={`${field}-error`}>
                        {errors[field]}
                      </span>
                    )}
                  </div>
                )
              })}
            </div>
          </>
        ) : (
          <p className="payment-method-note" role="status">
            {method === 'EFT'
              ? 'No bank transfer will be made here. Bank details are not available yet; your order will remain awaiting payment.'
              : 'You will not be redirected to PayPal yet. Your order will remain awaiting payment until PayPal is connected and payment is confirmed.'}
          </p>
        )}
        <button className="button card-payment-submit" type="submit" disabled={processing}>
          {processing ? 'Placing order…' : 'Place order'}
        </button>
      </form>
    </section>
  )
}
