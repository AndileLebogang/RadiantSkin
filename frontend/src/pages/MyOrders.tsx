import { useEffect, useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { FiArrowLeft, FiArrowRight, FiShoppingBag } from 'react-icons/fi'
import { useAuth } from '../AuthContext'
import { ApiError, createApi, money } from '../lib/store'
import type { Order } from '../lib/store'
import './my-orders.css'

const api = createApi(import.meta.env.VITE_API_BASE_URL || '/api')

function OrderHistory() {
  const [orders, setOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [expired, setExpired] = useState(false)
  const [attempt, setAttempt] = useState(0)

  useEffect(() => {
    const controller = new AbortController()
    api
      .myOrders(controller.signal)
      .then((result) => {
        if (!controller.signal.aborted) setOrders(result)
      })
      .catch((reason: unknown) => {
        if (controller.signal.aborted) return
        setError(reason instanceof Error ? reason.message : 'Your orders could not be loaded.')
        setExpired(reason instanceof ApiError && reason.status === 401)
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false)
      })
    return () => controller.abort()
  }, [attempt])

  if (loading) return <p role="status">Loading your orders…</p>
  if (error)
    return (
      <div className="order-history-message">
        <p role="alert">{error}</p>
        {expired ? (
          <Link className="button" to="/login?next=/my-orders">
            Log in
          </Link>
        ) : (
          <button
            className="button"
            onClick={() => {
              setLoading(true)
              setError('')
              setExpired(false)
              setAttempt((current) => current + 1)
            }}
          >
            Try again
          </button>
        )}
      </div>
    )
  if (!orders.length)
    return (
      <div className="order-history-message order-history-empty">
        <FiShoppingBag aria-hidden="true" />
        <h2>Your first order starts here.</h2>
        <p>Orders you place while signed in will appear here.</p>
        <Link className="button" to="/shop">
          Explore the collection <FiArrowRight aria-hidden="true" />
        </Link>
      </div>
    )
  return (
    <div className="order-history-list">
      <p className="order-history-count">
        {orders.length} {orders.length === 1 ? 'order' : 'orders'} · Newest first
      </p>
      {orders.map((order) => {
        const date = new Date(order.orderDate)
        const quantity = order.orderItems.reduce((sum, item) => sum + item.quantity, 0)
        return (
          <article
            className="order-history-card"
            key={order.orderId}
            aria-labelledby={`order-${order.orderId}`}
          >
            <header className="order-history-header">
              <div>
                <h2 id={`order-${order.orderId}`}>Order #{order.orderId}</h2>
                {!Number.isNaN(date.getTime()) && (
                  <p>
                    Placed{' '}
                    <time dateTime={order.orderDate}>
                      {date.toLocaleDateString('en-ZA', { dateStyle: 'long' })}
                    </time>
                  </p>
                )}
              </div>
              <span>
                {quantity} {quantity === 1 ? 'item' : 'items'}
              </span>
            </header>
            <ul className="order-history-items" aria-label={`Items in order ${order.orderId}`}>
              {order.orderItems.map((item) => (
                <li key={item.orderItemId}>
                  <div>
                    <span className="order-history-product">{item.product?.name || 'Product'}</span>
                    <span className="order-history-quantity">
                      Qty {item.quantity} · {money(item.unitPrice)} each
                    </span>
                  </div>
                  <strong>{money(item.unitPrice * item.quantity)}</strong>
                </li>
              ))}
            </ul>
            <div className="order-history-total">
              <span>Order total</span>
              <strong>{money(order.totalAmount)}</strong>
            </div>
          </article>
        )
      })}
    </div>
  )
}

export default function MyOrdersPage() {
  const { user, loading, sessionError, refresh } = useAuth()
  if (loading)
    return (
      <section className="page section" role="status">
        Loading your account…
      </section>
    )
  if (sessionError && !user)
    return (
      <section className="page section">
        <p role="alert">{sessionError}</p>
        <button className="button" onClick={refresh}>
          Try again
        </button>
      </section>
    )
  if (!user) return <Navigate to="/login?next=/my-orders" replace />
  if (user.role !== 'CUSTOMER') return <Navigate to="/account" replace />
  return (
    <section className="page section my-orders-page">
      <Link className="text-link order-history-back" to="/account">
        <FiArrowLeft aria-hidden="true" /> My account
      </Link>
      <div className="page-heading">
        <span className="eyebrow">Your purchase history</span>
        <h1>My orders.</h1>
        <p>Revisit your past purchases, all in one place.</p>
      </div>
      <OrderHistory key={user.userId} />
    </section>
  )
}
