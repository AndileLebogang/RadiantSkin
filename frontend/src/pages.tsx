import { useEffect, useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { Link, useLocation, useParams, useSearchParams } from 'react-router-dom'
import {
  FiArrowLeft,
  FiArrowRight,
  FiCheck,
  FiDroplet,
  FiGrid,
  FiHeart,
  FiInfo,
  FiPackage,
  FiSearch,
  FiSliders,
  FiTrash2,
  FiWind,
} from 'react-icons/fi'
import {
  CatalogueStatus,
  EmptyState,
  ProductCard,
  ProductImage,
  Quantity,
  ShopLink,
} from './components'
import { useStore } from './StoreContext'
import { useAuth } from './AuthContext'
import PaymentForm from './PaymentForm'
import ProductReviews from './ProductReviews'
import { categories, createApi, money, paymentMethodLabels } from './lib/store'
import type { CheckoutDetails, Order, PaymentMethod, Product } from './lib/store'

export { HomePage } from './HomePage'

const shopCategories = [
  { id: 'all', label: 'Shop all', detail: 'Everyday essentials', icon: FiGrid },
  { id: 'skin', label: 'Skin care', detail: 'Care for your face', icon: FiDroplet },
  { id: 'body', label: 'Body care', detail: 'From head to toe', icon: FiHeart },
  { id: 'hair', label: 'Hair care', detail: 'Your daily hair ritual', icon: FiWind },
]

export function ShopPage() {
  const { products } = useStore()
  const location = useLocation()
  const [params, setParams] = useSearchParams()
  const searchRef = useRef<HTMLInputElement>(null)
  const query = params.get('q') || ''
  const category = params.get('category') || 'all'
  const sort = params.get('sort') || 'featured'
  const inStock = params.get('stock') === 'true'
  const visible = products
    .filter(
      (product) =>
        (category === 'all' || product.category === category) &&
        (!inStock || product.stockQuantity > 0) &&
        `${product.name} ${product.brand} ${product.description}`
          .toLowerCase()
          .includes(query.toLowerCase()),
    )
    .sort((a, b) =>
      sort === 'price-asc'
        ? a.price - b.price
        : sort === 'price-desc'
          ? b.price - a.price
          : sort === 'name'
            ? a.name.localeCompare(b.name)
            : a.productId - b.productId,
    )
  function update(key: string, value: string) {
    const next = new URLSearchParams(params)
    if (value) next.set(key, value)
    else next.delete(key)
    setParams(next, { replace: true })
  }
  useEffect(() => {
    if (location.hash === '#search') searchRef.current?.focus()
  }, [location.hash])
  return (
    <div className="page section shop-page">
      <div className="page-heading">
        <span className="eyebrow">The RadiantSkin collection</span>
        <h1>The collection.</h1>
        <p>Skin, body, and hair care. A routine that’s entirely yours.</p>
      </div>
      <div className="shop-layout">
        <aside className="shop-toolbar" aria-label="Find your essentials">
          <div className="category-heading">
            <span className="eyebrow">Your everyday care</span>
            <h2>Shop by category</h2>
          </div>
          <div className="category-tabs" role="group" aria-label="Product category">
            {shopCategories.map(({ id, label, detail, icon: Icon }) => (
              <button
                key={id}
                aria-label={label}
                aria-pressed={category === id}
                className={category === id ? 'selected' : ''}
                onClick={() => update('category', id === 'all' ? '' : id)}
              >
                <span className="category-icon" aria-hidden="true">
                  <Icon />
                </span>
                <span className="category-copy">
                  <span>{label}</span>
                  <small>{detail}</small>
                </span>
                <FiCheck className="category-selected-mark" aria-hidden="true" />
              </button>
            ))}
          </div>
          <div className="category-search">
            <label htmlFor="search">Search products</label>
            <div className="search-field">
              <FiSearch aria-hidden="true" />
              <input
                id="search"
                ref={searchRef}
                type="search"
                placeholder="Name, brand or product…"
                value={query}
                onChange={(event) => update('q', event.target.value)}
              />
            </div>
          </div>
        </aside>
        <div className="shop-results">
          <div className="shop-filters">
            <span>
              {visible.length} {visible.length === 1 ? 'essential' : 'essentials'}
            </span>
            <div>
              <label className="checkbox-label">
                <input
                  type="checkbox"
                  checked={inStock}
                  onChange={(event) => update('stock', event.target.checked ? 'true' : '')}
                />{' '}
                In stock only
              </label>
              <label className="sort-label">
                <FiSliders aria-hidden="true" />
                <span className="sr-only">Sort products</span>
                <select value={sort} onChange={(event) => update('sort', event.target.value)}>
                  <option value="featured">Default order</option>
                  <option value="price-asc">Price: low to high</option>
                  <option value="price-desc">Price: high to low</option>
                  <option value="name">Name: A–Z</option>
                </select>
              </label>
            </div>
          </div>
          <CatalogueStatus>
            {visible.length ? (
              <div className="product-grid">
                {visible.map((product) => (
                  <ProductCard key={product.productId} product={product} />
                ))}
              </div>
            ) : (
              <EmptyState
                title={products.length ? 'No essentials found' : 'Our shelves are being prepared'}
                action={
                  products.length ? (
                    <button className="button secondary" onClick={() => setParams({})}>
                      Clear filters <FiArrowRight />
                    </button>
                  ) : undefined
                }
              >
                {products.length
                  ? 'Try a different search or clear your filters to see the whole collection.'
                  : 'Check back soon for skin, body, and hair care.'}
              </EmptyState>
            )}
          </CatalogueStatus>
        </div>
      </div>
    </div>
  )
}

export function ProductPage() {
  const { id } = useParams()
  const { products } = useStore()
  const product = products.find((product) => product.productId === Number(id))
  return (
    <section className="section page">
      <CatalogueStatus>
        {product ? (
          <ProductDetail key={product.productId} product={product} />
        ) : (
          <EmptyState title="This essential is no longer available" action={<ShopLink />}>
            Discover something else from the collection.
          </EmptyState>
        )}
      </CatalogueStatus>
    </section>
  )
}
function ProductDetail({ product }: { product: Product }) {
  const { bag, setQuantity, products, bagBusy } = useStore()
  const [quantity, setSelectedQuantity] = useState(1)
  const inBag = bag.find((item) => item.productId === product.productId)?.quantity || 0
  const remaining = Math.min(99 - inBag, product.stockQuantity - inBag)
  const related = products
    .filter((item) => item.category === product.category && item.productId !== product.productId)
    .slice(0, 4)
  return (
    <>
      <div className="breadcrumb">
        <Link to="/shop">The collection</Link>
        <span>/</span>
        <span>{product.name}</span>
      </div>
      <div className="product-detail">
        <ProductImage product={product} />
        <div className="product-information">
          <span className="eyebrow">
            {product.brand || 'RadiantSkin collection'} ·{' '}
            {categories.find((item) => item.id === product.category)?.label}
          </span>
          <h1>{product.name}</h1>
          <div className="detail-price">
            {money(product.price)}
            <span>{product.volumeMl > 0 ? `${product.volumeMl} ml` : ''}</span>
          </div>
          <p>{product.description || 'Make this essential part of your everyday routine.'}</p>
          <span className={`stock-note ${remaining <= 0 ? 'sold-out' : ''}`}>
            <span className="tiny-dot" />
            {product.stockQuantity === 0
              ? 'Currently out of stock'
              : remaining <= 0
                ? 'Available quantity already in your bag'
                : `${product.stockQuantity} available`}
          </span>
          <div className="detail-add">
            <Quantity
              quantity={quantity}
              max={Math.max(1, remaining)}
              name={product.name}
              onChange={setSelectedQuantity}
            />
            <button
              className="button"
              disabled={remaining <= 0 || bagBusy}
              onClick={() => setQuantity(product, inBag + Math.min(quantity, remaining))}
            >
              {remaining <= 0 ? 'Unavailable' : 'Add to bag'}
              <FiArrowRight />
            </button>
          </div>
          {inBag > 0 && (
            <Link className="text-link" to="/bag">
              {inBag} in your bag · View bag <FiArrowRight />
            </Link>
          )}
          <div className="detail-info">
            <FiInfo />
            <span>
              Your bag is saved on this device. Availability is checked against the latest
              catalogue.
            </span>
          </div>
          {product.instructions && (
            <details open>
              <summary>{product.category === 'skin' ? 'How to use' : 'Made for'}</summary>
              <p>{product.instructions}</p>
            </details>
          )}
          <ProductReviews key={product.productId} productId={product.productId} />
        </div>
      </div>
      {related.length > 0 && (
        <div className="related">
          <div className="section-heading">
            <h2>Complete your shelf.</h2>
          </div>
          <div className="product-grid">
            {related.map((item) => (
              <ProductCard key={item.productId} product={item} />
            ))}
          </div>
        </div>
      )}
    </>
  )
}

export function BagPage() {
  const { bag, products, setQuantity, remove, reload, bagBusy } = useStore()
  const lines = bag.map((item) => ({
    ...item,
    product: products.find((product) => product.productId === item.productId),
  }))
  const invalid = lines.some((line) => !line.product || line.product.stockQuantity < line.quantity)
  const total =
    lines.reduce(
      (sum, line) => sum + Math.round((line.product?.price || 0) * 100) * line.quantity,
      0,
    ) / 100
  return (
    <section className="section page">
      <div className="page-heading">
        <span className="eyebrow">Your everyday essentials</span>
        <h1>Your bag.</h1>
      </div>
      {bagBusy && !bag.length ? (
        <p role="status">Loading your bag…</p>
      ) : !bag.length ? (
        <EmptyState title="Your bag is waiting for a little care" action={<ShopLink />}>
          Explore the collection and make room for your next essential.
        </EmptyState>
      ) : (
        <CatalogueStatus>
          <div className="bag-layout">
            <div className="bag-items">
              {lines.map(({ productId, quantity, product }) => (
                <article className="bag-item" key={productId}>
                  {product ? (
                    <Link className="bag-item-image" to={`/products/${productId}`}>
                      <ProductImage product={product} />
                    </Link>
                  ) : (
                    <div className="missing-image">
                      <FiPackage />
                    </div>
                  )}
                  <div className="bag-item-info">
                    <span className="eyebrow">{product?.brand || 'Your essentials'}</span>
                    <h2>
                      {product ? (
                        <Link to={`/products/${productId}`}>{product.name}</Link>
                      ) : (
                        'Product no longer available'
                      )}
                    </h2>
                    {product && <span>{money(product.price)}</span>}
                    {(!product || quantity > product.stockQuantity) && (
                      <p className="inline-error">
                        {product?.stockQuantity
                          ? `Only ${product.stockQuantity} available. Please reduce the quantity.`
                          : 'This item is unavailable. Remove it to continue.'}
                      </p>
                    )}
                    <div className="bag-item-actions">
                      {product && (
                        <Quantity
                          quantity={quantity}
                          max={product.stockQuantity}
                          name={product.name}
                          disabled={bagBusy}
                          onChange={(value) => setQuantity(product, value)}
                        />
                      )}
                      <button
                        className="remove-button"
                        disabled={bagBusy}
                        onClick={() => remove(productId)}
                        aria-label={`Remove ${product?.name || 'unavailable product'} from bag`}
                      >
                        <FiTrash2 />
                        <span>Remove</span>
                      </button>
                    </div>
                  </div>
                  <span className="line-total">
                    {product ? money(product.price * quantity) : '—'}
                  </span>
                </article>
              ))}
              <Link className="text-link" to="/shop">
                <FiArrowLeft /> Continue exploring
              </Link>
            </div>
            <aside className="order-summary">
              <span className="eyebrow">Order summary</span>
              <h2>Your bag</h2>
              <div className="summary-line">
                <span>Items ({bag.reduce((sum, item) => sum + item.quantity, 0)})</span>
                <span>{money(total)}</span>
              </div>
              <div className="summary-line muted">
                <span>Delivery</span>
                <span>Complimentary</span>
              </div>
              <div className="summary-total">
                <span>Total</span>
                <span>{money(total)}</span>
              </div>
              {bagBusy ? (
                <p role="status">Saving your bag…</p>
              ) : invalid ? (
                <p className="inline-error" role="alert">
                  Update the unavailable items in your bag to continue.
                </p>
              ) : (
                <Link to="/checkout" className="button">
                  Review checkout <FiArrowRight />
                </Link>
              )}
              <p className="summary-note">Review your details and place your order at checkout.</p>
              <button className="text-link refresh-stock" onClick={reload}>
                Refresh prices & availability
              </button>
            </aside>
          </div>
        </CatalogueStatus>
      )}
    </section>
  )
}

const provinces = [
  'Eastern Cape',
  'Free State',
  'Gauteng',
  'KwaZulu-Natal',
  'Limpopo',
  'Mpumalanga',
  'North West',
  'Northern Cape',
  'Western Cape',
]
export function CheckoutPage() {
  const { user, loading: accountLoading } = useAuth()
  const { bag, products, clearBag, reload, bagBusy } = useStore()
  const [draft, setDraft] = useState<CheckoutDetails | null>(null)
  const [completedOrder, setCompletedOrder] = useState<Order | null>(null)
  const [completedPayment, setCompletedPayment] = useState<PaymentMethod>('CARD')
  const [placing, setPlacing] = useState(false)
  const [requestId, setRequestId] = useState(() => crypto.randomUUID())
  const formRef = useRef<HTMLFormElement>(null)
  const reviewRef = useRef<HTMLDivElement>(null)
  const invalid = bag.some((item) => {
    const product = products.find((product) => product.productId === item.productId)
    return !product || product.stockQuantity < item.quantity
  })
  const total =
    bag.reduce(
      (sum, item) =>
        sum +
        Math.round(
          (products.find((product) => product.productId === item.productId)?.price || 0) * 100,
        ) *
          item.quantity,
      0,
    ) / 100
  function review(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const details = Object.fromEntries(
      Array.from(new FormData(event.currentTarget).entries(), ([key, value]) => [
        key,
        String(value).trim(),
      ]),
    )
    setRequestId(crypto.randomUUID())
    setDraft(details as CheckoutDetails)
  }
  async function placeOrder(paymentMethod: PaymentMethod) {
    if (!draft || !bag.length || invalid || bagBusy)
      throw new Error('Review your bag before placing your order.')
    const api = createApi(import.meta.env.VITE_API_BASE_URL || '/api')
    // A retry uses the same request ID, even if the first response was lost.
    const order = await api.checkout({
      requestId,
      items: bag.map(({ productId, quantity }) => ({ productId, quantity })),
      details: draft,
      expectedTotal: total,
      paymentMethod,
      localPayment: true,
    })
    setCompletedPayment(paymentMethod)
    setCompletedOrder(order)
    setDraft(null)
    formRef.current?.reset()
    clearBag()
    reload()
  }
  useEffect(() => {
    if (draft) reviewRef.current?.focus()
  }, [draft])
  useEffect(() => {
    if (completedOrder) reviewRef.current?.focus()
  }, [completedOrder])
  return (
    <section className="section page">
      <Link className="text-link" to="/bag">
        <FiArrowLeft /> Back to your bag
      </Link>
      <div className="page-heading">
        <span className="eyebrow">The final details</span>
        <h1>Checkout.</h1>
      </div>
      {completedOrder ? (
        <div
          className="checkout-review card-payment-success"
          ref={reviewRef}
          tabIndex={-1}
          role="status"
        >
          <span className="eyebrow">
            <FiCheck /> Order received
          </span>
          <h2>Thank you for your order.</h2>
          <p>
            Order number: <strong>{completedOrder.orderId}</strong>
          </p>
          <p>Your order has been placed and your bag is now empty.</p>
          <p>
            Payment method: <strong>{paymentMethodLabels[completedPayment]}</strong>
          </p>
          <p>
            Payment status:{' '}
            <strong>
              {completedOrder.payment?.status === 'SUCCESSFUL'
                ? 'Complete'
                : 'Awaiting confirmation'}
            </strong>
          </p>
          {completedOrder.payment && (
            <p>
              Payment reference: <strong>{completedOrder.payment.reference}</strong>
            </p>
          )}
          <p>
            Total: <strong>{money(completedOrder.totalAmount)}</strong>
          </p>
          <div className="checkout-success-actions">
            {user?.role === 'CUSTOMER' && (
              <Link className="button" to="/my-orders">
                View my orders
              </Link>
            )}
            <Link className="button" to={`/orders?order=${completedOrder.orderId}`}>
              Track your order <FiArrowRight />
            </Link>
            <Link className="text-link" to="/shop">
              Continue shopping <FiArrowRight />
            </Link>
          </div>
        </div>
      ) : bagBusy ? (
        <p role="status">Loading your bag…</p>
      ) : !bag.length ? (
        <EmptyState title="Start with a little care" action={<ShopLink />}>
          Add an essential to your bag before preparing checkout.
        </EmptyState>
      ) : (
        <CatalogueStatus>
          {accountLoading ? (
            <p role="status">Loading your checkout…</p>
          ) : invalid ? (
            <EmptyState
              title="Your bag needs a quick update"
              action={
                <Link className="button" to="/bag">
                  Review your bag <FiArrowRight />
                </Link>
              }
            >
              Some products are no longer available in the quantity selected.
            </EmptyState>
          ) : (
            <>
              <div className="checkout-layout">
                <form ref={formRef} className="checkout-form" onSubmit={review} hidden={!!draft}>
                  {!user && (
                    <p className="checkout-account-note">
                      Already have an account?{' '}
                      <Link className="text-link" to="/login?next=/checkout">
                        Log in for a quicker checkout
                      </Link>
                      .
                    </p>
                  )}
                  <fieldset>
                    <legend>
                      <span>01</span> Your details
                    </legend>
                    <div className="form-grid">
                      <Field
                        label="First name"
                        name="firstName"
                        autoComplete="given-name"
                        defaultValue={user?.firstName}
                      />
                      <Field
                        label="Last name"
                        name="lastName"
                        autoComplete="family-name"
                        defaultValue={user?.lastName}
                      />
                      <Field
                        label="Email address"
                        name="email"
                        type="email"
                        autoComplete="email"
                        defaultValue={user?.email}
                      />
                      <Field
                        label="Phone number"
                        name="phoneNumber"
                        type="tel"
                        autoComplete="tel"
                        defaultValue={user?.phoneNumber}
                        pattern="[+0-9\s\(\)\-]{7,25}"
                      />
                    </div>
                  </fieldset>
                  <fieldset>
                    <legend>
                      <span>02</span> Delivery address
                    </legend>
                    <div className="form-grid">
                      <Field
                        label="Street address"
                        name="street"
                        autoComplete="street-address"
                        wide
                      />
                      <Field label="City" name="city" autoComplete="address-level2" />
                      <label>
                        Province
                        <select name="province" required defaultValue="">
                          <option value="" disabled>
                            Select a province
                          </option>
                          {provinces.map((province) => (
                            <option key={province}>{province}</option>
                          ))}
                        </select>
                      </label>
                      <Field
                        label="Postal code"
                        name="postalCode"
                        autoComplete="postal-code"
                        pattern="[0-9]{4}"
                      />
                      <label>
                        Country
                        <input
                          name="country"
                          value="South Africa"
                          readOnly
                          autoComplete="country-name"
                        />
                      </label>
                    </div>
                  </fieldset>
                  <button className="button" type="submit">
                    Review details <FiArrowRight />
                  </button>
                  <p className="form-note">
                    Check your contact details and delivery address before continuing.
                  </p>
                </form>
                {draft && (
                  <div className="checkout-review" ref={reviewRef} tabIndex={-1}>
                    <>
                      <span className="eyebrow">
                        <FiCheck /> Ready for review
                      </span>
                      <h2>Your checkout details</h2>
                      <div>
                        <h3>Contact</h3>
                        <p>
                          {draft.firstName} {draft.lastName}
                          <br />
                          {draft.email}
                          <br />
                          {draft.phoneNumber}
                        </p>
                      </div>
                      <div>
                        <h3>Delivery address</h3>
                        <p>
                          {draft.street}
                          <br />
                          {draft.city}, {draft.province} {draft.postalCode}
                          <br />
                          {draft.country}
                        </p>
                      </div>
                      <button
                        className="text-link"
                        disabled={placing}
                        onClick={() => {
                          setDraft(null)
                          window.setTimeout(
                            () => formRef.current?.querySelector('input')?.focus(),
                            0,
                          )
                        }}
                      >
                        <FiArrowLeft /> Edit details
                      </button>
                      <PaymentForm
                        total={total}
                        requestId={requestId}
                        onPlaceOrder={placeOrder}
                        onProcessingChange={setPlacing}
                      />
                    </>
                  </div>
                )}
                <aside className="order-summary">
                  <span className="eyebrow">Your essentials</span>
                  <h2>Order summary</h2>
                  {bag.map((item) => (
                    <div className="summary-line" key={item.productId}>
                      <span>
                        {products.find((product) => product.productId === item.productId)?.name}{' '}
                        <small>× {item.quantity}</small>
                      </span>
                      <span>
                        {money(
                          (products.find((product) => product.productId === item.productId)
                            ?.price || 0) * item.quantity,
                        )}
                      </span>
                    </div>
                  ))}
                  <div className="summary-line muted">
                    <span>Delivery</span>
                    <span>Complimentary</span>
                  </div>
                  <div className="summary-total">
                    <span>Total</span>
                    <span>{money(total)}</span>
                  </div>
                  <p className="summary-note">
                    Your bag will be cleared once your order is confirmed.
                  </p>
                </aside>
              </div>
            </>
          )}
        </CatalogueStatus>
      )}
    </section>
  )
}
function Field({
  label,
  name,
  type = 'text',
  autoComplete,
  pattern,
  wide = false,
  defaultValue,
}: {
  label: string
  name: string
  type?: string
  autoComplete: string
  pattern?: string
  wide?: boolean
  defaultValue?: string
}) {
  return (
    <label className={wide ? 'wide' : ''}>
      {label}
      <input
        name={name}
        defaultValue={defaultValue}
        type={type}
        autoComplete={autoComplete}
        required
        maxLength={200}
        pattern={pattern || (type === 'text' ? '.*\\S.*' : undefined)}
      />
    </label>
  )
}

export function OrdersPage() {
  const [params] = useSearchParams()
  const [order, setOrder] = useState<Order | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const controller = useRef<AbortController | null>(null)
  useEffect(() => () => controller.current?.abort(), [])
  async function lookup(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const id = new FormData(event.currentTarget).get('orderId')?.toString().trim() || ''
    controller.current?.abort()
    const request = new AbortController()
    controller.current = request
    setLoading(true)
    setError('')
    setOrder(null)
    try {
      const result = await createApi(import.meta.env.VITE_API_BASE_URL || '/api').order(
        id,
        request.signal,
      )
      if (!request.signal.aborted) setOrder(result)
    } catch (error) {
      if (!request.signal.aborted)
        setError(error instanceof Error ? error.message : 'Your order could not be loaded.')
    } finally {
      if (!request.signal.aborted) setLoading(false)
    }
  }
  const date = order ? new Date(order.orderDate) : null
  return (
    <section className="section page orders-page">
      <div className="page-heading">
        <span className="eyebrow">Keep up with your order</span>
        <h1>Order status.</h1>
        <p>Enter your order number to see its latest status.</p>
      </div>
      <form className="order-lookup" onSubmit={lookup}>
        <label htmlFor="orderId">Order number</label>
        <div>
          <input
            id="orderId"
            name="orderId"
            defaultValue={params.get('order') || ''}
            inputMode="numeric"
            pattern="[1-9][0-9]*"
            required
            placeholder="e.g. 1001"
            maxLength={16}
            onChange={() => {
              setError('')
              setOrder(null)
            }}
          />
          <button className="button" disabled={loading}>
            {loading ? 'Finding your order…' : 'Track order'}
            <FiArrowRight />
          </button>
        </div>
      </form>
      {error && (
        <p className="error-message" role="alert">
          {error}
        </p>
      )}
      {loading && <p role="status">Looking up your order…</p>}
      {order && (
        <article className="order-result" aria-live="polite">
          <div className="section-heading">
            <div>
              <span className="eyebrow">Order #{order.orderId}</span>
              <h2>{order.status.replaceAll('_', ' ').toLowerCase()}</h2>
            </div>
            <FiPackage />
          </div>
          {date && !Number.isNaN(date.getTime()) && (
            <p>Placed {date.toLocaleDateString('en-ZA', { dateStyle: 'long' })}</p>
          )}
          {order.payment && (
            <div className="payment-receipt-details">
              <div className="summary-line">
                <span>Payment method</span>
                <strong>{paymentMethodLabels[order.payment.method]}</strong>
              </div>
              <div className="summary-line">
                <span>Payment status</span>
                <strong>
                  {order.payment.status === 'SUCCESSFUL'
                    ? 'Complete'
                    : order.payment.status === 'FAILED'
                      ? 'Unsuccessful'
                      : 'Awaiting confirmation'}
                </strong>
              </div>
              <div className="summary-line">
                <span>Payment reference</span>
                <strong>{order.payment.reference}</strong>
              </div>
            </div>
          )}
          {order.orderItems.map((item) => (
            <div className="summary-line" key={item.orderItemId}>
              <span>
                {item.product?.name || 'Product'} × {item.quantity}
              </span>
              <span>{money(item.unitPrice * item.quantity)}</span>
            </div>
          ))}
          <div className="summary-total">
            <span>Order total</span>
            <span>{money(order.totalAmount)}</span>
          </div>
          <p className="summary-note">Keep your order number handy to check the latest updates.</p>
        </article>
      )}
    </section>
  )
}
export function NotFoundPage() {
  return (
    <section className="section page">
      <EmptyState title="A little off the beaten path" action={<ShopLink />}>
        We couldn’t find this page. Let’s get you back to your essentials.
      </EmptyState>
    </section>
  )
}
