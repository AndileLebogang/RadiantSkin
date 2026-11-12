import { useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { useAuth } from '../AuthContext'

export default function AccountPage() {
  const { user, loading, sessionError, refresh, signOut } = useAuth()
  const [leaving, setLeaving] = useState(false)
  const [error, setError] = useState('')

  async function leave() {
    if (leaving) return
    setLeaving(true)
    setError('')
    try {
      await signOut()
    } catch (error) {
      setError(error instanceof Error ? error.message : 'We could not log you out.')
    } finally {
      setLeaving(false)
    }
  }
  if (loading)
    return (
      <div className="page section" role="status">
        Loading your account…
      </div>
    )
  if (sessionError && !user)
    return (
      <div className="page section">
        <p role="alert">{sessionError}</p>
        <button className="button" onClick={refresh}>
          Try again
        </button>
      </div>
    )
  if (!user) return <Navigate to="/login?next=/account" replace />
  return (
    <div className="page section account-page">
      <div className="account-intro">
        <span className="eyebrow">Your RadiantSkin account</span>
        <h1>Hello, {user.firstName || 'there'}.</h1>
        <p>You’re logged in. Your details are ready for your next order.</p>
        <Link className="text-link" to="/shop">
          Explore the collection
        </Link>
      </div>
      <div className="account-panel">
        <span className="eyebrow">A little more you</span>
        <h2>Your details.</h2>
        <dl className="account-details">
          <div>
            <dt>Name</dt>
            <dd>
              {user.firstName} {user.lastName}
            </dd>
          </div>
          <div>
            <dt>Email address</dt>
            <dd>{user.email}</dd>
          </div>
          {user.phoneNumber && (
            <div>
              <dt>Phone number</dt>
              <dd>{user.phoneNumber}</dd>
            </div>
          )}
        </dl>
        <div className="checkout-success-actions">
          <Link className="button" to="/bag">
            View your bag
          </Link>
          <Link className="text-link" to="/orders">
            Track an order
          </Link>
          {user.role === 'CUSTOMER' && (
            <Link className="text-link" to="/my-reviews">
              My reviews
            </Link>
          )}
        </div>
        {error && (
          <p className="account-alert" role="alert">
            {error}
          </p>
        )}
        <button className="text-link account-logout" disabled={leaving} onClick={leave}>
          {leaving ? 'Logging out…' : 'Log out'}
        </button>
      </div>
    </div>
  )
}
