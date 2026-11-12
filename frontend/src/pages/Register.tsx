import { useEffect, useRef, useState } from 'react'
import type { ChangeEvent, FormEvent } from 'react'
import { Link } from 'react-router-dom'

type Field =
  | 'firstName'
  | 'lastName'
  | 'email'
  | 'phoneNumber'
  | 'employeeNumber'
  | 'password'
  | 'confirmPassword'
type Values = Record<Field, string>

const empty: Values = {
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  employeeNumber: '',
  password: '',
  confirmPassword: '',
}
function validate(values: Values, admin: boolean): Partial<Record<Field, string>> {
  const errors: Partial<Record<Field, string>> = {}
  if (!values.firstName.trim()) errors.firstName = 'Enter your first name.'
  if (!values.lastName.trim()) errors.lastName = 'Enter your last name.'
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email.trim()))
    errors.email = 'Enter a valid email address.'
  if (admin) {
    if (!values.employeeNumber.trim()) errors.employeeNumber = 'Enter your employee number.'
    else if (values.employeeNumber.trim().length > 100)
      errors.employeeNumber = 'Use no more than 100 characters.'
    if (values.firstName.trim().length > 100) errors.firstName = 'Use no more than 100 characters.'
    if (values.lastName.trim().length > 100) errors.lastName = 'Use no more than 100 characters.'
    if (values.email.trim().length > 254) errors.email = 'Use no more than 254 characters.'
    if (new TextEncoder().encode(values.password).length > 72)
      errors.password = 'Use a password of no more than 72 bytes.'
  } else if (!values.phoneNumber.trim()) errors.phoneNumber = 'Enter your phone number.'
  if (values.password.length < 8) errors.password = 'Use at least 8 characters.'
  if (values.confirmPassword !== values.password)
    errors.confirmPassword = 'The passwords do not match.'
  else if (!values.confirmPassword) errors.confirmPassword = 'Confirm your password.'
  return errors
}

export default function RegisterPage({ admin = false }: { admin?: boolean }) {
  const [values, setValues] = useState<Values>(empty)
  const [touched, setTouched] = useState<Partial<Record<Field, boolean>>>({})
  const [submitted, setSubmitted] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [serverError, setServerError] = useState('')
  const [emailTaken, setEmailTaken] = useState(false)
  const [created, setCreated] = useState(false)
  const errorRef = useRef<HTMLDivElement>(null)
  const errors = validate(values, admin)
  const fields: Field[] = [
    'firstName',
    'lastName',
    'email',
    admin ? 'employeeNumber' : 'phoneNumber',
    'password',
    'confirmPassword',
  ]
  const loginPath = admin ? '/login?next=/admin' : '/login'

  useEffect(() => {
    if (serverError) errorRef.current?.focus()
  }, [serverError])

  function change(event: ChangeEvent<HTMLInputElement>) {
    const field = event.currentTarget.name as Field
    const value = event.currentTarget.value
    setValues((current) => ({ ...current, [field]: value }))
    if (serverError) setServerError('')
    if (field === 'email') setEmailTaken(false)
  }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (submitting) return
    setSubmitted(true)
    setServerError('')
    setEmailTaken(false)
    const firstError = fields.find((field) => errors[field])
    if (firstError) {
      document.getElementById(firstError)?.focus()
      return
    }

    setSubmitting(true)
    try {
      const base = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/$/, '')
      const response = await fetch(`${base}/${admin ? 'admin' : 'customer'}/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
        body: JSON.stringify({
          firstName: values.firstName.trim(),
          lastName: values.lastName.trim(),
          email: values.email.trim().toLowerCase(),
          ...(admin
            ? { employeeNumber: values.employeeNumber.trim() }
            : { phoneNumber: values.phoneNumber.trim() }),
          password: values.password,
        }),
      })
      if (!response.ok) {
        if (response.status === 400) {
          const message = await response.text()
          setServerError(
            message.includes('already registered')
              ? 'An account with this email already exists.'
              : 'Please check your details and try again.',
          )
          if (message.includes('already registered')) setEmailTaken(true)
        } else {
          setServerError('We could not create your account right now. Please try again.')
        }
        return
      }
      setValues(empty)
      setCreated(true)
    } catch {
      setServerError('We could not reach the store. Check your connection and try again.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="page section account-page">
      <div className="account-intro">
        <span className="eyebrow">{admin ? 'RadiantSkin staff' : 'Your everyday care'}</span>
        <h1>{admin ? 'Create an admin account.' : 'Create an account.'}</h1>
        <p>
          {admin
            ? 'Register to manage products, orders, payments and customer reviews.'
            : 'Keep your details ready for the RadiantSkin experiences ahead.'}
        </p>
        <a className="text-link" href="#account-form">
          Start with your details
        </a>
      </div>
      <div className="account-panel" id="account-form">
        {created ? (
          <div className="account-success" role="status">
            <span className="eyebrow">Welcome to RadiantSkin</span>
            <h2>
              {admin ? 'Your admin account has been created.' : 'Your account has been created.'}
            </h2>
            <p>
              {admin
                ? 'Log in with your new email and password to open the admin portal.'
                : 'Your account is ready. Log in to continue shopping.'}
            </p>
            <Link className="button" to={loginPath}>
              Log in
            </Link>
          </div>
        ) : (
          <>
            <span className="eyebrow">{admin ? 'Admin registration' : 'Join RadiantSkin'}</span>
            <h2>Your details.</h2>
            <p className="account-panel-intro">All fields are required.</p>
            {serverError && (
              <div className="account-alert" role="alert" tabIndex={-1} ref={errorRef}>
                {serverError}
              </div>
            )}
            <form onSubmit={submit} noValidate>
              <div className="account-fields">
                {(
                  [
                    ['firstName', 'First name', 'given-name', 'text'],
                    ['lastName', 'Last name', 'family-name', 'text'],
                    ['email', 'Email address', 'email', 'email'],
                    ['phoneNumber', 'Phone number', 'tel', 'tel'],
                    ['password', 'Password', 'new-password', 'password'],
                    ['confirmPassword', 'Confirm password', 'new-password', 'password'],
                  ] as const
                ).map(([originalField, originalLabel, originalAutocomplete, originalType]) => {
                  const field: Field =
                    admin && originalField === 'phoneNumber' ? 'employeeNumber' : originalField
                  const label = field === 'employeeNumber' ? 'Employee number' : originalLabel
                  const autocomplete = field === 'employeeNumber' ? 'off' : originalAutocomplete
                  const type = field === 'employeeNumber' ? 'text' : originalType
                  const errorMessage =
                    touched[field] || submitted
                      ? errors[field] ||
                        (field === 'email' && emailTaken
                          ? 'Use a different email address.'
                          : undefined)
                      : undefined
                  return (
                    <div className="account-field" key={field}>
                      <label htmlFor={field}>{label}</label>
                      <input
                        id={field}
                        name={field}
                        type={type}
                        autoComplete={autocomplete}
                        value={values[field]}
                        onChange={change}
                        disabled={submitting}
                        onBlur={() => setTouched((current) => ({ ...current, [field]: true }))}
                        aria-invalid={Boolean(errorMessage)}
                        aria-describedby={
                          errorMessage
                            ? `${field}-error${field === 'password' ? ' password-hint' : ''}`
                            : field === 'password'
                              ? 'password-hint'
                              : undefined
                        }
                        required
                      />
                      {field === 'password' && (
                        <span className="account-hint" id="password-hint">
                          At least 8 characters.
                        </span>
                      )}
                      {errorMessage && (
                        <span className="account-field-error" id={`${field}-error`}>
                          {errorMessage}
                        </span>
                      )}
                    </div>
                  )
                })}
              </div>
              <button className="button account-submit" type="submit" disabled={submitting}>
                {submitting
                  ? 'Creating account…'
                  : admin
                    ? 'Create admin account'
                    : 'Create account'}
              </button>
            </form>
            <p className="account-switch">
              Already have an account?{' '}
              <Link className="text-link" to={loginPath}>
                Log in
              </Link>
            </p>
            <p className="account-switch">
              <Link className="text-link" to={admin ? '/register' : '/admin/register'}>
                {admin ? 'Register as a customer' : 'Register as admin'}
              </Link>
            </p>
          </>
        )}
      </div>
    </div>
  )
}
