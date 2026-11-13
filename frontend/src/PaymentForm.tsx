import { useEffect, useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { FiCheck, FiCopy, FiCreditCard, FiMail, FiShield } from 'react-icons/fi'
import { money, paymentMethodLabels } from './lib/store'
import type { PaymentMethod } from './lib/store'
import { cardFields, emptyCard, transferReference, validateCard } from './lib/payment'
import type { CardDetails } from './lib/payment'

export default function PaymentForm({
  total,
  requestId,
  onPlaceOrder,
  onProcessingChange,
}: {
  total: number
  requestId: string
  onPlaceOrder: (method: PaymentMethod) => Promise<void>
  onProcessingChange: (processing: boolean) => void
}) {
  const [details, setDetails] = useState<CardDetails>(emptyCard)
  const [method, setMethod] = useState<PaymentMethod>('CARD')
  const [methodLocked, setMethodLocked] = useState(false)
  const [attempted, setAttempted] = useState(false)
  const [processing, setProcessing] = useState(false)
  const [serverError, setServerError] = useState('')
  const [paypalEmail, setPaypalEmail] = useState('')
  const [approving, setApproving] = useState(false)
  const [transferred, setTransferred] = useState(false)
  const [copyNotice, setCopyNotice] = useState('')
  const submitting = useRef(false)
  const errorRef = useRef<HTMLDivElement>(null)
  const approvalRef = useRef<HTMLDivElement>(null)
  const errors = validateCard(details)
  const validEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(paypalEmail.trim())
  const reference = transferReference(requestId)

  useEffect(() => {
    if (serverError) errorRef.current?.focus()
  }, [serverError])
  useEffect(() => {
    if (approving) approvalRef.current?.focus()
  }, [approving])

  async function copyReference() {
    try {
      await navigator.clipboard.writeText(reference)
      setCopyNotice('Payment reference copied.')
    } catch {
      setCopyNotice('Select the payment reference above to copy it.')
    }
  }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (submitting.current) return
    setAttempted(true)
    setServerError('')
    if (method === 'CARD') {
      const first = cardFields.find((field) => errors[field])
      if (first) {
        document.getElementById(first)?.focus()
        return
      }
    }
    if (method === 'EFT' && !transferred) {
      document.getElementById('transfer-confirmation')?.focus()
      return
    }
    if (method === 'PAYPAL') {
      if (!validEmail) {
        document.getElementById('paypal-email')?.focus()
        return
      }
      if (!approving) {
        setApproving(true)
        setAttempted(false)
        return
      }
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
      setDetails(emptyCard)
      setAttempted(false)
      submitting.current = false
      setProcessing(false)
      onProcessingChange(false)
    }
  }

  return (
    <section className="card-payment" aria-labelledby="payment-heading">
      <span className="eyebrow">03 / Payment</span>
      <h3 id="payment-heading">How would you like to pay?</h3>
      <p className="card-payment-intro">Choose your preferred method to complete your order.</p>
      {serverError && (
        <div className="account-alert" role="alert" tabIndex={-1} ref={errorRef}>
          {serverError}
        </div>
      )}
      <form onSubmit={submit} autoComplete="off" noValidate>
        <fieldset className="payment-methods" disabled={processing || methodLocked || approving}>
          <legend className="sr-only">Choose a payment method</legend>
          {(Object.keys(paymentMethodLabels) as PaymentMethod[]).map((value) => (
            <label className={`payment-option ${method === value ? 'selected' : ''}`} key={value}>
              <input
                type="radio"
                name="paymentMethod"
                aria-label={paymentMethodLabels[value]}
                value={value}
                checked={method === value}
                onChange={() => {
                  setMethod(value)
                  setDetails(emptyCard)
                  setAttempted(false)
                  setServerError('')
                  setTransferred(false)
                  setPaypalEmail('')
                  setCopyNotice('')
                }}
              />
              <span className="payment-option-copy">
                <strong>{paymentMethodLabels[value]}</strong>
                <small>
                  {value === 'CARD'
                    ? 'Credit or debit card'
                    : value === 'EFT'
                      ? 'Pay by bank transfer'
                      : 'Review and approve your payment'}
                </small>
              </span>
              {method === value && <FiCheck aria-hidden="true" />}
            </label>
          ))}
        </fieldset>
        {methodLocked && serverError && (
          <p className="form-note">Your details are ready. Try placing your order again.</p>
        )}
        {method === 'CARD' && (
          <>
            <div className="payment-section-title">
              <FiCreditCard aria-hidden="true" />
              <h3>Card details</h3>
            </div>
            <div className="card-payment-fields">
              {(
                [
                  ['cardholder', 'Name on card', 'text', 'text'],
                  ['cardNumber', 'Card number', 'text', 'numeric'],
                  ['expiry', 'Expiry date (MM/YY)', 'text', 'numeric'],
                  ['securityCode', 'Security code', 'password', 'numeric'],
                ] as const
              ).map(([field, label, type, inputMode]) => (
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
                    onChange={(event) => {
                      let value = event.target.value
                      if (field === 'expiry') {
                        const digits = value.replace(/\D/g, '').slice(0, 4)
                        value =
                          digits.length > 2 ? `${digits.slice(0, 2)}/${digits.slice(2)}` : digits
                      }
                      if (field === 'securityCode') value = value.replace(/\D/g, '').slice(0, 4)
                      setDetails((current) => ({ ...current, [field]: value }))
                      setServerError('')
                    }}
                    disabled={processing}
                    aria-invalid={attempted && Boolean(errors[field])}
                    aria-describedby={attempted && errors[field] ? `${field}-error` : undefined}
                    maxLength={
                      field === 'cardNumber'
                        ? undefined
                        : field === 'expiry'
                          ? 5
                          : field === 'securityCode'
                            ? 4
                            : 80
                    }
                    placeholder={
                      field === 'expiry'
                        ? 'MM/YY'
                        : field === 'cardNumber'
                          ? '1234 5678 9012 3456'
                          : undefined
                    }
                    required={field !== 'cardNumber'}
                  />
                  {attempted && errors[field] && (
                    <span className="account-field-error" id={`${field}-error`}>
                      {errors[field]}
                    </span>
                  )}
                </div>
              ))}
            </div>
            <p className="payment-privacy">
              <FiShield aria-hidden="true" /> Card details are never saved.
            </p>
          </>
        )}
        {method === 'EFT' && (
          <div className="transfer-panel">
            <h3>Bank transfer</h3>
            <p>Use the details below and include your payment reference.</p>
            <dl className="payment-bank-details">
              <div>
                <dt>Account holder</dt>
                <dd>RadiantSkin School Store</dd>
              </div>
              <div>
                <dt>Bank</dt>
                <dd>RadiantSkin Practice Bank</dd>
              </div>
              <div>
                <dt>Account number</dt>
                <dd>0000000000</dd>
              </div>
              <div>
                <dt>Branch code</dt>
                <dd>000000</dd>
              </div>
              <div>
                <dt>Amount</dt>
                <dd>{money(total)}</dd>
              </div>
              <div>
                <dt>Payment reference</dt>
                <dd className="transfer-reference">
                  <span>{reference}</span>
                  <button
                    type="button"
                    className="icon-button"
                    aria-label="Copy payment reference"
                    onClick={copyReference}
                  >
                    <FiCopy />
                  </button>
                </dd>
              </div>
            </dl>
            <p className="payment-copy-feedback" role="status">
              {copyNotice}
            </p>
            <label className="transfer-confirmation">
              <input
                id="transfer-confirmation"
                type="checkbox"
                checked={transferred}
                disabled={processing}
                onChange={(event) => setTransferred(event.target.checked)}
                aria-invalid={attempted && !transferred}
                aria-describedby={attempted && !transferred ? 'transfer-error' : undefined}
              />
              <span>I have completed the transfer.</span>
            </label>
            {attempted && !transferred && (
              <p id="transfer-error" className="account-field-error">
                Confirm the transfer before placing your order.
              </p>
            )}
          </div>
        )}
        {method === 'PAYPAL' &&
          (approving ? (
            <div className="paypal-approval" ref={approvalRef} tabIndex={-1}>
              <span className="eyebrow">PayPal</span>
              <h3>Review your payment</h3>
              <dl className="payment-bank-details">
                <div>
                  <dt>Paying with</dt>
                  <dd>{paypalEmail}</dd>
                </div>
                <div>
                  <dt>Merchant</dt>
                  <dd>RadiantSkin</dd>
                </div>
                <div>
                  <dt>Total</dt>
                  <dd>{money(total)}</dd>
                </div>
              </dl>
              <button
                type="button"
                className="text-link"
                disabled={processing}
                onClick={() => {
                  setApproving(false)
                  window.setTimeout(() => document.getElementById('paypal-email')?.focus(), 0)
                }}
              >
                Change PayPal account
              </button>
            </div>
          ) : (
            <div className="paypal-panel">
              <div className="payment-section-title">
                <FiMail aria-hidden="true" />
                <h3>PayPal account</h3>
              </div>
              <p>Enter your account email, then review your payment.</p>
              <div className="card-payment-field">
                <label htmlFor="paypal-email">PayPal email address</label>
                <input
                  id="paypal-email"
                  type="email"
                  autoComplete="email"
                  value={paypalEmail}
                  disabled={processing}
                  onChange={(event) => setPaypalEmail(event.target.value)}
                  aria-invalid={attempted && !validEmail}
                  aria-describedby={attempted && !validEmail ? 'paypal-email-error' : undefined}
                  required
                />
                {attempted && !validEmail && (
                  <span className="account-field-error" id="paypal-email-error">
                    Enter a valid PayPal email address.
                  </span>
                )}
              </div>
            </div>
          ))}
        <div className="payment-total">
          <span>Total to pay</span>
          <strong>{money(total)}</strong>
        </div>
        <button className="button card-payment-submit" type="submit" disabled={processing}>
          {processing
            ? 'Confirming your order…'
            : method === 'PAYPAL'
              ? approving
                ? 'Approve & place order'
                : 'Review payment'
              : 'Place order'}
        </button>
      </form>
    </section>
  )
}
