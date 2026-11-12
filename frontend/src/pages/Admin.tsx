import { useCallback, useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../AuthContext'
import { admin } from '../lib/admin'
import type {
  AdminCustomer,
  AdminOrder,
  AdminProduct,
  AdminReview,
  AdminReviewPage,
  ProductCategory,
  ReviewModerationStatus,
} from '../lib/admin'
import { money } from '../lib/store'
import './admin.css'

type Tab = 'orders' | 'products' | 'customers' | 'reviews'

const message = (error: unknown) =>
  error instanceof Error ? error.message : 'Something went wrong. Please try again.'

export default function AdminPage() {
  const { user, loading } = useAuth()
  const [tab, setTab] = useState<Tab>('orders')

  if (loading)
    return (
      <section className="section page">
        <p role="status">Loading…</p>
      </section>
    )
  if (!user || user.role !== 'ADMIN')
    return (
      <section className="section page">
        <div className="page-heading">
          <span className="eyebrow">Staff only</span>
          <h1>Admin portal.</h1>
          <p>Log in with an admin account to continue.</p>
        </div>
        <Link className="button" to="/login?next=/admin">
          Log in
        </Link>
      </section>
    )

  return (
    <section className="section page admin-page">
      <div className="page-heading">
        <span className="eyebrow">Staff only</span>
        <h1>Admin portal.</h1>
        <p>
          Signed in as {user.firstName} {user.lastName}
        </p>
      </div>
      <div className="admin-tabs" role="group" aria-label="Admin sections">
        {(['orders', 'products', 'customers', 'reviews'] as Tab[]).map((item) => (
          <button key={item} aria-pressed={tab === item} onClick={() => setTab(item)}>
            {item[0].toUpperCase() + item.slice(1)}
          </button>
        ))}
      </div>
      {tab === 'orders' && <OrdersTab />}
      {tab === 'products' && <ProductsTab />}
      {tab === 'customers' && <CustomersTab />}
      {tab === 'reviews' && <ReviewsTab />}
    </section>
  )
}

function OrdersTab() {
  const [orders, setOrders] = useState<AdminOrder[]>([])
  const [orderStatuses, setOrderStatuses] = useState<string[]>([])
  const [paymentStatuses, setPaymentStatuses] = useState<string[]>([])
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    try {
      const [o, os, ps] = await Promise.all([
        admin.orders(),
        admin.orderStatuses(),
        admin.paymentStatuses(),
      ])
      setOrders(o)
      setOrderStatuses(os)
      setPaymentStatuses(ps)
      setError('')
    } catch (e) {
      setError(message(e))
    }
  }, [])
  useEffect(() => {
    void load()
  }, [load])

  async function change(action: () => Promise<void>) {
    try {
      await action()
      await load()
    } catch (e) {
      setError(message(e))
    }
  }

  return (
    <div>
      {error && (
        <p className="error-message" role="alert">
          {error}
        </p>
      )}
      <p>{orders.length} orders</p>
      <div className="admin-table-wrap">
        <table className="admin-table">
          <thead>
            <tr>
              <th>Order</th>
              <th>Customer</th>
              <th>Items</th>
              <th>Total</th>
              <th>Order status</th>
              <th>Payment</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => {
              const payment = order.payments[order.payments.length - 1]
              return (
                <tr key={order.orderId}>
                  <td>
                    #{order.orderId}
                    <br />
                    <small>{order.date.slice(0, 10)}</small>
                  </td>
                  <td>
                    {order.customerName}
                    <br />
                    <small>{order.email}</small>
                    <br />
                    <small>{order.address}</small>
                  </td>
                  <td>
                    {order.items.map((item) => (
                      <div key={item}>{item}</div>
                    ))}
                  </td>
                  <td>{money(order.total)}</td>
                  <td>
                    <select
                      aria-label={`Status for order ${order.orderId}`}
                      value={order.status}
                      onChange={(event) =>
                        change(() => admin.setOrderStatus(order.orderId, event.target.value))
                      }
                    >
                      {orderStatuses.map((status) => (
                        <option key={status}>{status}</option>
                      ))}
                    </select>
                  </td>
                  <td>
                    {payment ? (
                      <>
                        {money(payment.amount)} · {payment.method}
                        <br />
                        <select
                          aria-label={`Payment status for order ${order.orderId}`}
                          value={payment.status}
                          onChange={(event) =>
                            change(() => admin.setPaymentStatus(order.orderId, event.target.value))
                          }
                        >
                          {paymentStatuses.map((status) => (
                            <option key={status}>{status}</option>
                          ))}
                        </select>
                      </>
                    ) : (
                      'No payment'
                    )}
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>
    </div>
  )
}

type Form = {
  category: ProductCategory
  name: string
  brand: string
  description: string
  price: string
  stock: string
  imageUrl: string
  volumeMl: string
  extra: string
}
const emptyForm: Form = {
  category: 'skin',
  name: '',
  brand: '',
  description: '',
  price: '',
  stock: '0',
  imageUrl: '',
  volumeMl: '0',
  extra: '',
}
const extraLabel: Record<ProductCategory, string> = {
  skin: 'Usage instructions',
  hair: 'Hair concern',
  body: 'Skin concern',
}

function ProductsTab() {
  const [products, setProducts] = useState<AdminProduct[]>([])
  const [error, setError] = useState('')
  const [editing, setEditing] = useState<number | null>(null)
  const [form, setForm] = useState<Form>(emptyForm)
  const [saving, setSaving] = useState(false)

  const load = useCallback(async () => {
    try {
      setProducts(await admin.products())
      setError('')
    } catch (e) {
      setError(message(e))
    }
  }, [])
  useEffect(() => {
    void load()
  }, [load])

  function field(name: keyof Form, value: string) {
    setForm((current) => ({ ...current, [name]: value }))
  }
  function startEdit(product: AdminProduct) {
    setEditing(product.productId)
    setForm({
      category: product.category,
      name: product.name,
      brand: product.brand,
      description: product.description,
      price: String(product.price),
      stock: String(product.stock),
      imageUrl: product.imageUrl,
      volumeMl: String(product.volumeMl),
      extra: product.extra,
    })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
  function cancel() {
    setEditing(null)
    setForm(emptyForm)
  }
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setSaving(true)
    setError('')
    const input = {
      category: form.category,
      name: form.name.trim(),
      brand: form.brand.trim(),
      description: form.description.trim(),
      price: Number(form.price),
      stock: Number(form.stock),
      imageUrl: form.imageUrl.trim(),
      volumeMl: Number(form.volumeMl),
      extra: form.extra.trim(),
    }
    try {
      if (editing === null) await admin.createProduct(input)
      else await admin.updateProduct(editing, input)
      cancel()
      await load()
    } catch (e) {
      setError(message(e))
    } finally {
      setSaving(false)
    }
  }
  async function remove(product: AdminProduct) {
    if (!window.confirm(`Delete "${product.name}"? This cannot be undone.`)) return
    try {
      await admin.deleteProduct(product.productId)
      if (editing === product.productId) cancel()
      await load()
    } catch (e) {
      setError(message(e))
    }
  }

  return (
    <div>
      {error && (
        <p className="error-message" role="alert">
          {error}
        </p>
      )}
      <form className="admin-form" onSubmit={submit}>
        <h2>{editing === null ? 'Add a product' : `Edit product #${editing}`}</h2>
        <label>
          Category
          <select
            value={form.category}
            disabled={editing !== null}
            onChange={(event) => field('category', event.target.value)}
          >
            <option value="skin">Skin care</option>
            <option value="body">Body care</option>
            <option value="hair">Hair care</option>
          </select>
        </label>
        <label>
          Name
          <input required value={form.name} onChange={(e) => field('name', e.target.value)} />
        </label>
        <label>
          Brand
          <input value={form.brand} onChange={(e) => field('brand', e.target.value)} />
        </label>
        <label>
          Price (R)
          <input
            required
            type="number"
            min={0}
            step="0.01"
            value={form.price}
            onChange={(e) => field('price', e.target.value)}
          />
        </label>
        <label>
          Stock
          <input
            required
            type="number"
            min={0}
            max={100000}
            value={form.stock}
            onChange={(e) => field('stock', e.target.value)}
          />
        </label>
        <label>
          Volume (ml)
          <input
            type="number"
            min={0}
            value={form.volumeMl}
            onChange={(e) => field('volumeMl', e.target.value)}
          />
        </label>
        <label className="wide">
          Image URL
          <input value={form.imageUrl} onChange={(e) => field('imageUrl', e.target.value)} />
        </label>
        <label className="wide">
          Description
          <textarea
            rows={3}
            value={form.description}
            onChange={(e) => field('description', e.target.value)}
          />
        </label>
        <label className="wide">
          {extraLabel[form.category]}
          <input value={form.extra} onChange={(e) => field('extra', e.target.value)} />
        </label>
        <div className="admin-form-actions">
          <button className="button" disabled={saving}>
            {saving ? 'Saving…' : editing === null ? 'Add product' : 'Save changes'}
          </button>
          {editing !== null && (
            <button type="button" className="button secondary" onClick={cancel}>
              Cancel
            </button>
          )}
        </div>
      </form>
      <div className="admin-table-wrap">
        <table className="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.productId}>
                <td>{product.productId}</td>
                <td>
                  {product.name}
                  <br />
                  <small>{product.brand}</small>
                </td>
                <td>{product.category}</td>
                <td>{money(product.price)}</td>
                <td>{product.stock}</td>
                <td>
                  <button className="button secondary" onClick={() => startEdit(product)}>
                    Edit
                  </button>{' '}
                  <button className="button secondary" onClick={() => remove(product)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

function CustomersTab() {
  const [customers, setCustomers] = useState<AdminCustomer[]>([])
  const [error, setError] = useState('')
  useEffect(() => {
    admin
      .customers()
      .then(setCustomers)
      .catch((e) => setError(message(e)))
  }, [])
  return (
    <div>
      {error && (
        <p className="error-message" role="alert">
          {error}
        </p>
      )}
      <div className="admin-table-wrap">
        <table className="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
            </tr>
          </thead>
          <tbody>
            {customers.map((customer) => (
              <tr key={customer.userId}>
                <td>{customer.userId}</td>
                <td>{customer.name}</td>
                <td>{customer.email}</td>
                <td>{customer.phone}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

function ReviewsTab() {
  const [status, setStatus] = useState<ReviewModerationStatus>('HIDDEN')
  const [reviews, setReviews] = useState<AdminReview[]>([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [loadingReviews, setLoadingReviews] = useState(true)
  const [savingId, setSavingId] = useState<number | null>(null)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const loadReviews = useCallback(async () => {
    setLoadingReviews(true)
    setError('')

    try {
      const result: AdminReviewPage = await admin.moderationReviews(status, page, 10)

      setReviews(result.content)
      setTotalPages(result.totalPages)
      setTotalElements(result.totalElements)
    } catch (e) {
      setError(message(e))
    } finally {
      setLoadingReviews(false)
    }
  }, [status, page])

  useEffect(() => {
    void loadReviews()
  }, [loadReviews])

  async function changeStatus(reviewId: number, newStatus: ReviewModerationStatus) {
    setSavingId(reviewId)
    setError('')
    setSuccess('')

    try {
      if (newStatus === 'APPROVED') {
        await admin.approveReview(reviewId)
        setSuccess(`Review #${reviewId} has been approved.`)
      } else {
        await admin.hideReview(reviewId)
        setSuccess(`Review #${reviewId} has been hidden.`)
      }

      await loadReviews()
    } catch (e) {
      setError(message(e))
    } finally {
      setSavingId(null)
    }
  }

  function changeFilter(newStatus: ReviewModerationStatus) {
    setStatus(newStatus)
    setPage(0)
    setSuccess('')
  }

  return (
    <div className="admin-reviews">
      <div className="admin-review-heading">
        <div>
          <h2>Review moderation</h2>
          <p>Review customer feedback and manage its visibility.</p>
        </div>
        <p>
          <strong>{totalElements}</strong> reviews
        </p>
      </div>

      <div className="admin-tabs" role="group" aria-label="Review status filter">
        <button
          type="button"
          aria-pressed={status === 'HIDDEN'}
          onClick={() => changeFilter('HIDDEN')}
        >
          Hidden
        </button>
        <button
          type="button"
          aria-pressed={status === 'APPROVED'}
          onClick={() => changeFilter('APPROVED')}
        >
          Approved
        </button>
      </div>

      {error && (
        <p className="error-message" role="alert">
          {error}
        </p>
      )}

      {success && (
        <p className="admin-review-success" role="status">
          {success}
        </p>
      )}

      {loadingReviews ? (
        <p role="status">Loading reviews...</p>
      ) : reviews.length === 0 ? (
        <div className="admin-review-empty">
          <h3>No {status.toLowerCase()} reviews</h3>
          <p>There are currently no reviews in this category.</p>
        </div>
      ) : (
        <>
          <div className="admin-review-list">
            {reviews.map((review) => (
              <article className="admin-review-card" key={review.reviewId}>
                <div className="admin-review-card-header">
                  <div>
                    <h3>Review #{review.reviewId}</h3>
                    <p>Product #{review.productId}</p>
                  </div>
                  <span className="admin-review-rating">
                    {'★'.repeat(review.rating)}
                    {'☆'.repeat(5 - review.rating)}
                    <small>{review.rating}/5</small>
                  </span>
                </div>

                <p className="admin-review-comment">
                  {review.comment || 'No written comment provided.'}
                </p>

                <div className="admin-review-details">
                  <span>Date: {review.reviewDate || 'Not available'}</span>
                  <span>Helpful votes: {review.helpfulCount}</span>
                  {review.verifiedPurchase && (
                    <span className="admin-review-verified">Verified purchase</span>
                  )}
                </div>

                <div className="admin-review-actions">
                  {status === 'HIDDEN' ? (
                    <button
                      type="button"
                      className="button"
                      disabled={savingId === review.reviewId}
                      onClick={() => changeStatus(review.reviewId, 'APPROVED')}
                    >
                      {savingId === review.reviewId ? 'Updating...' : 'Approve review'}
                    </button>
                  ) : (
                    <button
                      type="button"
                      className="button secondary"
                      disabled={savingId === review.reviewId}
                      onClick={() => changeStatus(review.reviewId, 'HIDDEN')}
                    >
                      {savingId === review.reviewId ? 'Updating...' : 'Hide review'}
                    </button>
                  )}
                </div>
              </article>
            ))}
          </div>

          {totalPages > 1 && (
            <div className="admin-review-pagination">
              <button
                type="button"
                className="button secondary"
                disabled={page === 0}
                onClick={() => setPage((current) => current - 1)}
              >
                Previous
              </button>

              <span>
                Page {page + 1} of {totalPages}
              </span>

              <button
                type="button"
                className="button secondary"
                disabled={page >= totalPages - 1}
                onClick={() => setPage((current) => current + 1)}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  )
}
