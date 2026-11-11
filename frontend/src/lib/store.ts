import { resolveProductImage } from './productImages'

export type Category = 'skin' | 'hair' | 'body'
export type Product = {
  productId: number
  name: string
  description: string
  brand: string
  price: number
  stockQuantity: number
  imageUrl: string
  volumeMl: number
  category: Category
  instructions: string
  reviews: { reviewId: number; rating: number; comment: string }[]
}
export type BagItem = { productId: number; quantity: number }
export type CheckoutDetails = {
  firstName: string
  lastName: string
  email: string
  phoneNumber: string
  street: string
  city: string
  province: string
  postalCode: string
  country: string
}
export type CheckoutSubmission = {
  requestId: string
  items: BagItem[]
  details: CheckoutDetails
  expectedTotal: number
}
export type Review = {
  reviewId: number
  productId: number
  rating: number
  comment: string
  reviewDate: string
  verifiedPurchase: boolean
  helpfulCount: number
  updatedAt: string | null
}

export type ReviewPage = {
  content: Review[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export type ReviewStatistics = {
  averageRating: number
  totalReviews: number
  distribution: {
    rating: number
    count: number
  }[]

}
export type ReviewSubmission = {
  productId: number
  rating: number
  comment: string
}

export type ReviewUpdate = {
  rating: number
  comment: string
}

export type Order = {
  orderId: number
  orderDate: string
  status: string
  totalAmount: number
  orderItems: {
    orderItemId: number
    quantity: number
    unitPrice: number
    product: { name: string } | null
  }[]
}
const record = (value: unknown): Record<string, unknown> => {
  if (typeof value !== 'object' || value === null || Array.isArray(value))
    throw new Error('The store returned an unexpected response. Please try again later.')
  return value as Record<string, unknown>
}
const positiveId = (value: unknown) =>
  typeof value === 'number' && Number.isSafeInteger(value) && value > 0
const text = (value: unknown) => (typeof value === 'string' ? value : '')
function normalizeOrder(value: unknown): Order {
  const order = record(value)
  if (!positiveId(order.orderId)) throw new ApiError('The store did not return an order number.')
  return {
    orderId: order.orderId as number,
    orderDate: text(order.orderDate),
    status: text(order.status) || 'PENDING',
    totalAmount: amount(order.totalAmount),
    orderItems: Array.isArray(order.orderItems)
      ? order.orderItems.map((value) => {
          const item = record(value)
          return {
            orderItemId: Number(item.orderItemId),
            quantity: Number(item.quantity),
            unitPrice: amount(item.unitPrice),
            product: item.product ? { name: text(record(item.product).name) } : null,
          }
        })
      : [],
  }
}
function amount(value: unknown): number {
  if ((typeof value !== 'number' && typeof value !== 'string') || value === '')
    throw new Error('A price is missing from the store response.')
  const number = Number(value)
  if (!Number.isFinite(number) || number < 0)
    throw new Error('The store returned an invalid price.')
  return number
}
export function normalizeProduct(value: unknown): Product {
  const product = record(value)
  if (!positiveId(product.productId) || !text(product.name).trim())
    throw new Error('A product could not be loaded. Please try again later.')
  const category = 'hairConcern' in product ? 'hair' : 'skinConcern' in product ? 'body' : 'skin'
  return {
    productId: product.productId as number,
    name: text(product.name),
    description: text(product.description),
    brand: text(product.brand),
    price: amount(product.price),
    stockQuantity:
      typeof product.stockQuantity === 'number' && Number.isInteger(product.stockQuantity)
        ? Math.max(0, product.stockQuantity)
        : 0,
    imageUrl: safeImageUrl(text(product.imageUrl)),
    volumeMl: typeof product.volumeMl === 'number' ? Math.max(0, product.volumeMl) : 0,
    category,
    instructions: text(product.usageInstructions ?? product.hairConcern ?? product.skinConcern),
    reviews: Array.isArray(product.reviews)
      ? product.reviews.flatMap((value) => {
          const review = record(value)
          return typeof review.rating === 'number' && review.rating >= 1 && review.rating <= 5
            ? [
                {
                  reviewId: Number(review.reviewId),
                  rating: review.rating,
                  comment: text(review.comment),
                },
              ]
            : []
        })
      : [],
  }
}
export function safeImageUrl(value: string): string {
  return resolveProductImage(value)
}
function normalizeReview(value: unknown): Review {
  const review = record(value)

  if (
    !positiveId(review.reviewId) ||
    !positiveId(review.productId) ||
    !Number.isInteger(review.rating) ||
    Number(review.rating) < 1 ||
    Number(review.rating) > 5 ||
    !text(review.comment).trim()
  ) {
    throw new Error('A review could not be loaded. Please try again later.')
  }

  return {
    reviewId: review.reviewId as number,
    productId: review.productId as number,
    rating: review.rating as number,
    comment: text(review.comment),
    reviewDate: text(review.reviewDate),
    verifiedPurchase: review.verifiedPurchase === true,
    helpfulCount:
      typeof review.helpfulCount === 'number' &&
      Number.isInteger(review.helpfulCount) &&
      review.helpfulCount >= 0
        ? review.helpfulCount
        : 0,
    updatedAt: review.updatedAt === null ? null : text(review.updatedAt),
  }
}

function normalizeReviewPage(value: unknown): ReviewPage {
  const page = record(value)

  if (
    !Array.isArray(page.content) ||
    !Number.isInteger(page.page) ||
    !Number.isInteger(page.size) ||
    !Number.isInteger(page.totalElements) ||
    !Number.isInteger(page.totalPages) ||
    typeof page.last !== 'boolean'
  ) {
    throw new ApiError('The reviews could not be loaded correctly.')
  }

  return {
    content: page.content.map(normalizeReview),
    page: page.page as number,
    size: page.size as number,
    totalElements: page.totalElements as number,
    totalPages: page.totalPages as number,
    last: page.last,
  }
}

function normalizeReviewStatistics(value: unknown): ReviewStatistics {
  const statistics = record(value)

  if (
    typeof statistics.averageRating !== 'number' ||
    !Number.isFinite(statistics.averageRating) ||
    typeof statistics.totalReviews !== 'number' ||
    !Number.isInteger(statistics.totalReviews) ||
    !Array.isArray(statistics.distribution)
  ) {
    throw new ApiError('The review statistics could not be loaded correctly.')
  }

  const distribution = statistics.distribution.map((item) => {
    const rating = record(item)

    if (
      !Number.isInteger(rating.rating) ||
      (rating.rating as number) < 1 ||
      (rating.rating as number) > 5 ||
      !Number.isInteger(rating.count) ||
      (rating.count as number) < 0
    ) {
      throw new ApiError('The review statistics could not be loaded correctly.')
    }

    return {
      rating: rating.rating as number,
      count: rating.count as number,
    }
  })

  return {
    averageRating: statistics.averageRating,
    totalReviews: statistics.totalReviews,
    distribution,
  }
}
export class ApiError extends Error {
  status: number
  constructor(message: string, status = 0) {
    super(message)
    this.status = status
  }
}
export function createApi(baseUrl: string, fetcher?: typeof fetch) {
  async function request(path: string, signal?: AbortSignal, init?: RequestInit): Promise<unknown> {
    try {
      const response = await (fetcher ?? fetch)(`${baseUrl.replace(/\/$/, '')}${path}`, {
        ...init,
        credentials: 'include',
        headers: { Accept: 'application/json', ...init?.headers },
        cache: 'no-store',
        signal: AbortSignal.any([AbortSignal.timeout(15000), ...(signal ? [signal] : [])]),
      })
      if (!response.ok) {
        if (path === '/checkout/place' && (response.status === 400 || response.status === 409)) {
          const error = await response.json().catch(() => null)
          throw new ApiError(
            error && typeof error.message === 'string'
              ? error.message
              : 'Review your bag and details, then try again.',
            response.status,
          )
        }
        throw new ApiError(
          response.status === 401
            ? 'Your session has expired. Log in again to post your review.'
            : response.status === 403 && path === '/review/submit'
              ? 'Only customer accounts can post reviews.'
              : response.status === 400 && path === '/review/submit'
                ? 'Check your rating and review, then try again.'
                : response.status === 404 && path.startsWith('/order/')
                  ? 'We couldn’t find that order. Check the order number and try again.'
                  : 'The store is temporarily unavailable. Please try again in a moment.',
          response.status,
        )
      }
      if (response.status === 204) return null

      const body: unknown = await response.json()
      if (body === null)
        throw new ApiError(
          'We couldn’t find that order. Check the order number and try again.',
          404,
        )
      return body
    } catch (error) {
      if (signal?.aborted) throw error
      if (error instanceof ApiError) throw error
      if (error instanceof SyntaxError)
        throw new ApiError('The store could not load this information. Please try again later.')
      throw new ApiError(
        'We can’t reach the store right now. Please check your connection and try again.',
      )
    }
  }
  return {
    async products(signal?: AbortSignal): Promise<Product[]> {
      const body = await request('/product/getAll', signal)
      if (!Array.isArray(body))
        throw new ApiError('The catalogue could not be loaded. Please try again later.')
      return body.map(normalizeProduct)
    },
    async order(id: string, signal?: AbortSignal): Promise<Order> {
      if (!/^[1-9]\d*$/.test(id) || !Number.isSafeInteger(Number(id)))
        throw new ApiError('Enter a valid order number.', 400)
      return normalizeOrder(await request(`/order/read/${encodeURIComponent(id)}`, signal))
    },
    async checkout(input: CheckoutSubmission): Promise<Order> {
      // Send only order details. Card inputs never leave the payment form.
      return normalizeOrder(
        await request('/checkout/place', undefined, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(input),
        }),
      )
    },
    async reviews(
  productId: number,
  signal?: AbortSignal,
  sort = 'recent',
  page = 0,
  size = 5,
): Promise<ReviewPage> {
  if (!positiveId(productId)) {
    throw new ApiError('Choose a valid product.', 400)
  }

  if (
    !['recent', 'highest', 'lowest', 'helpful'].includes(sort) ||
    !Number.isInteger(page) ||
    page < 0 ||
    !Number.isInteger(size) ||
    size < 1 ||
    size > 50
  ) {
    throw new ApiError('Invalid review pagination or sorting options.', 400)
  }

  const query = new URLSearchParams({
    sort,
    page: String(page),
    size: String(size),
  })

  const body = await request(
    `/review/product/${productId}?${query.toString()}`,
    signal,
  )

  return normalizeReviewPage(body)
},

async reviewStatistics(productId: number): Promise<ReviewStatistics> {
  if (!positiveId(productId)) {
    throw new ApiError('Choose a valid product.', 400)
  }

  const body = await request(`/review/product/${productId}/statistics`)

  return normalizeReviewStatistics(body)
},

    async submitReview(input: ReviewSubmission): Promise<Review> {
  const body = await request('/review/submit', undefined, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })
  return normalizeReview(body)
},

async myReviews(): Promise<Review[]> {
  const body = await request('/review/my-reviews')

  if (!Array.isArray(body)) {
    throw new ApiError('Invalid review response.', 500)
  }

  return body.map(normalizeReview)
},

async updateReview(
  reviewId: number,
  input: ReviewUpdate,
): Promise<Review> {
  if (!positiveId(reviewId)) {
    throw new ApiError('Choose a valid review.', 400)
  }

  const body = await request(`/review/my-reviews/${reviewId}`, undefined, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })

  return normalizeReview(body)
},

async deleteReview(reviewId: number): Promise<void> {
  if (!positiveId(reviewId)) {
    throw new ApiError('Choose a valid review.', 400)
  }

  await request(`/review/my-reviews/${reviewId}`, undefined, {
    method: 'DELETE',
  })
},
  }
}

export function readBag(raw: string | null): BagItem[] {
  try {
    const value: unknown = JSON.parse(raw ?? '[]')
    if (!Array.isArray(value)) return []
    const seen = new Set<number>()
    return value.flatMap((item) => {
      if (
        !item ||
        !positiveId(item.productId) ||
        !Number.isInteger(item.quantity) ||
        item.quantity < 1 ||
        seen.has(item.productId)
      )
        return []
      seen.add(item.productId)
      return [{ productId: item.productId, quantity: Math.min(99, item.quantity) }]
    })
  } catch {
    return []
  }
}
export function changeBag(
  bag: BagItem[],
  productId: number,
  quantity: number,
  stock: number,
): BagItem[] {
  const nextQuantity = Math.max(0, Math.min(Math.floor(quantity), Math.floor(stock), 99))
  if (!Number.isFinite(nextQuantity) || !positiveId(productId)) return bag
  if (nextQuantity === 0) return bag.filter((item) => item.productId !== productId)
  return bag.some((item) => item.productId === productId)
    ? bag.map((item) =>
        item.productId === productId ? { productId, quantity: nextQuantity } : item,
      )
    : [...bag, { productId, quantity: nextQuantity }]
}

export const money = (value: number) =>
  new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR' }).format(value)
export const categories: { id: Category; label: string; description: string }[] = [
  { id: 'skin', label: 'Skin care', description: 'Make time for your daily ritual.' },
  { id: 'body', label: 'Body care', description: 'A little care, from head to toe.' },
  { id: 'hair', label: 'Hair care', description: 'Good days start with your hair.' },
]
