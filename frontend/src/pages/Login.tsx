import { useEffect, useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { Link, Navigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../AuthContext'

export default function LoginPage() {
  const { user, loading, signIn } = useAuth()
  const [params] = useSearchParams()
  const requested = params.get('next') || '/account'
  const customerDestination = ['/account', '/shop', '/checkout', '/bag'].includes(requested)
    ? requested
    : '/account'
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const busy = useRef(false)
  const errorRef = useRef<HTMLDivElement>(null)
  useEffect(() => {
    if (error) errorRef.current?.focus()
  }, [error])

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (busy.current) return
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim()) || !password) {
      setError('Enter your email address and password.')
      return
    }
    busy.current = true
    setSubmitting(true)
    setError('')
    try {
      await signIn(email, password)
    } catch (error) {
      setError(
        error instanceof Error ? error.message : 'We could not log you in. Please try again.',
      )
    } finally {
      setPassword('')
      setSubmitting(false)
      busy.current = false
    }
  }

  if (loading)
    return (
      <div className="page section" role="status">
        Checking your account…
      </div>
    )
  // Admins go to the admin portal, customers go to their account page.
  if (user) return <Navigate to={user.role === 'ADMIN' ? '/admin' : customerDestination} replace />
  return (
    <div className="page section account-page">
      <div className="account-intro">
        <span className="eyebrow">Your everyday care</span>
        <h1>Welcome back.</h1>
        <p>Log in to your RadiantSkin account and pick up where you left off.</p>
        <Link className="text-link" to="/register">
          New here? Create an account
        </Link>
        <p className="account-switch">
          <Link className="text-link" to="/admin/register">
            Register as admin
          </Link>
        </p>
      </div>
      <div className="account-panel">
        <span className="eyebrow">Your account</span>
        <h2>Log in.</h2>
        <p className="account-panel-intro">Enter the email and password you used to register.</p>
        {error && (
          <div className="account-alert" role="alert" tabIndex={-1} ref={errorRef}>
            {error}
          </div>
        )}
        <form onSubmit={submit} noValidate>
          <div className="account-fields account-fields-single">
            <div className="account-field">
              <label htmlFor="login-email">Email address</label>
              <input
                id="login-email"
                type="email"
                autoComplete="username"
                value={email}
                onChange={(event) => {
                  setEmail(event.target.value)
                  setError('')
                }}
                disabled={submitting}
                required
              />
            </div>
            <div className="account-field">
              <label htmlFor="login-password">Password</label>
              <input
                id="login-password"
                type="password"
                autoComplete="current-password"
                value={password}
                onChange={(event) => {
                  setPassword(event.target.value)
                  setError('')
                }}
                disabled={submitting}
                required
              />
            </div>
          </div>
          <button className="button account-submit" type="submit" disabled={submitting}>
            {submitting ? 'Logging in…' : 'Log in'}
          </button>
        </form>
      </div>
    </div>
  )
}
