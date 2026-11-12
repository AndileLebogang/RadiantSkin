const base = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/$/, '')

export type AdminPayment = {
  paymentId: number
  amount: number
  method: string
  status: string
  reference: string
}
export type AdminOrder = {
  orderId: number
  date: string
  status: string
  total: number
  customerName: string
  email: string
  address: string
  items: string[]
  payments: AdminPayment[]
}
export type ProductCategory = 'skin' | 'hair' | 'body'
export type AdminProduct = {
  productId: number
  category: ProductCategory
  name: string
  description: string
  brand: string
  price: number
  stock: number
  imageUrl: string
  volumeMl: number
  extra: string
}
export type ProductInput = Omit<AdminProduct, 'productId'>
export type AdminCustomer = { userId: number; name: string; email: string; phone: string }
export type AdminReview = {
  reviewId: number
  productId: number
  rating: number
  comment: string
  reviewDate: string
  verifiedPurchase: boolean
  helpfulCount: number
  updatedAt: string | null
}

export type ReviewModerationStatus = 'APPROVED' | 'HIDDEN'

export type AdminReviewPage = {
  content: AdminReview[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

async function call<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response
  try {
    response = await fetch(`${base}/admin-portal${path}`, {
      ...init,
      credentials: 'include',
      cache: 'no-store',
      headers: {
        Accept: 'application/json',
        ...(init?.body ? { 'Content-Type': 'application/json' } : {}),
      },
    })
  } catch {
    throw new Error('We could not reach the server. Check that the backend is running.')
  }
  if (response.status === 401 || response.status === 403)
    throw new Error('Log in with an admin account to use this page.')
  if (response.status === 409)
    throw new Error(
      'This product has been ordered before, so it cannot be deleted. Set its stock to 0 instead.',
    )
  if (response.status === 400) throw new Error('Check the details and try again.')
  if (!response.ok) throw new Error('Something went wrong. Please try again.')
  const text = await response.text()
  return (text ? JSON.parse(text) : undefined) as T
}

async function reviewCall<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response

  try {
    response = await fetch(`${base}/review${path}`, {
      ...init,
      credentials: 'include',
      cache: 'no-store',
      headers: {
        Accept: 'application/json',
        ...(init?.body ? { 'Content-Type': 'application/json' } : {}),
      },
    })
  } catch {
    throw new Error('We could not reach the server. Check that the backend is running.')
  }

  if (response.status === 401 || response.status === 403) {
    throw new Error('Administrator access is required.')
  }

  if (!response.ok) {
    throw new Error('Could not complete the review moderation request.')
  }

  const text = await response.text()
  return (text ? JSON.parse(text) : undefined) as T
}

const moderateReview = (id: number, status: ReviewModerationStatus) =>
  reviewCall<AdminReview>(`/admin/${id}/status`, {
    method: 'PUT',
    body: JSON.stringify({ status }),
  })

const send = <T>(method: string, path: string, body?: unknown) =>
  call<T>(path, { method, body: body === undefined ? undefined : JSON.stringify(body) })

export const admin = {
  orders: () => call<AdminOrder[]>('/orders'),
  orderStatuses: () => call<string[]>('/order-statuses'),
  paymentStatuses: () => call<string[]>('/payment-statuses'),
  setOrderStatus: (id: number, status: string) =>
    send<void>('PUT', `/orders/${id}/status`, { status }),
  setPaymentStatus: (id: number, status: string) =>
    send<void>('PUT', `/orders/${id}/payment-status`, { status }),
  products: () => call<AdminProduct[]>('/products'),
  createProduct: (input: ProductInput) => send<AdminProduct>('POST', '/products', input),
  updateProduct: (id: number, input: ProductInput) =>
    send<AdminProduct>('PUT', `/products/${id}`, input),
  deleteProduct: (id: number) => send<void>('DELETE', `/products/${id}`),
  customers: () => call<AdminCustomer[]>('/customers'),
    moderationReviews: (status: ReviewModerationStatus, page = 0, size = 10) =>
    reviewCall<AdminReviewPage>(
      `/admin/moderation?status=${status}&page=${page}&size=${size}`,
    ),
  approveReview: (id: number) => moderateReview(id, 'APPROVED'),
  hideReview: (id: number) => moderateReview(id, 'HIDDEN'),
}
