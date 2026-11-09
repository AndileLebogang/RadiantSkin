import { useEffect, useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { FiStar } from 'react-icons/fi'
import { ApiError, createApi } from './lib/store'
import type { Review } from './lib/store'
import { useAuth } from './AuthContext'

const api = createApi(import.meta.env.VITE_API_BASE_URL || '/api')

export default function ProductReviews({ productId }: { productId: number }) {
  const { user, loading: accountLoading, sessionError, refresh } = useAuth()
  const loginUrl = `/login?next=${encodeURIComponent(`/products/${productId}`)}`
  const [reviews, setReviews] = useState<Review[]>([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [retry, setRetry] = useState(0)
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState('')
  const [submitted, setSubmitted] = useState(false)
  const formRef = useRef<HTMLFormElement>(null)

  useEffect(() => {
    const controller = new AbortController()
    api
      .reviews(productId, controller.signal)
      .then((result) => {
        if (!controller.signal.aborted) {
          setReviews(result)
          setLoadError('')
        }
      })
      .catch((error) => {
        if (!controller.signal.aborted)
          setLoadError(error instanceof Error ? error.message : 'Reviews could not be loaded.')
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false)
      })
    return () => controller.abort()
  }, [productId, retry])

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (submitting || accountLoading || user?.role !== 'CUSTOMER') return
    const data = new FormData(event.currentTarget)
    setSubmitting(true)
    setSubmitError('')
    setSubmitted(false)
    try {
      const saved = await api.submitReview({
        productId,
        rating: Number(data.get('rating')),
        comment: String(data.get('comment') || '').trim(),
      })
      setReviews((current) => [saved, ...current])
      formRef.current?.reset()
      setSubmitted(true)
    } catch (error) {
      setSubmitError(error instanceof Error ? error.message : 'Your review could not be sent.')
      if (error instanceof ApiError && (error.status === 401 || error.status === 403))
        await refresh()
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <details className="product-reviews" open>
      <summary>Reviews ({reviews.length})</summary>
      {loading && <p role="status">Loading reviews…</p>}
      {loadError && (
        <div className="reviews-feedback" role="alert">
          <p>{loadError}</p>
          <button
            className="text-link"
            type="button"
            onClick={() => {
              setLoading(true)
              setRetry((value) => value + 1)
            }}
          >
            Try loading reviews again
          </button>
        </div>
      )}
      {!loading &&
        !loadError &&
        (reviews.length ? (
          <div className="review-list">
            {reviews.map((review) => (
              <div className="review" key={review.reviewId}>
                <span aria-label={`${review.rating} out of 5 stars`}>
                  {Array.from({ length: review.rating }, (_, index) => (
                    <FiStar key={index} aria-hidden="true" />
                  ))}
                </span>
                <p>{review.comment}</p>
              </div>
            ))}
          </div>
        ) : (
          <p>No reviews yet. Be the first to share your experience.</p>
        ))}
      {submitError && (
        <p className="reviews-feedback" role="alert">
          {submitError}
        </p>
      )}
      {accountLoading ? (
        <p role="status">Checking your account…</p>
      ) : sessionError ? (
        <div className="reviews-feedback" role="alert">
          <p>{sessionError}</p>
          <button className="text-link" type="button" onClick={() => void refresh()}>
            Try checking your account again
          </button>
        </div>
      ) : user?.role === 'CUSTOMER' ? (
        <form className="review-form" ref={formRef} onSubmit={submit}>
          <h3>Write a review</h3>
          <p>Reviewing as {user.firstName || user.email}.</p>
          <label htmlFor="review-rating">Rating</label>
          <select id="review-rating" name="rating" defaultValue="" required>
            <option value="" disabled>
              Choose a rating
            </option>
            {[5, 4, 3, 2, 1].map((rating) => (
              <option key={rating} value={rating}>
                {rating} {rating === 1 ? 'star' : 'stars'}
              </option>
            ))}
          </select>
          <label htmlFor="review-comment">Your review</label>
          <textarea
            id="review-comment"
            name="comment"
            minLength={3}
            maxLength={240}
            rows={4}
            required
          />
          {submitted && (
            <p className="reviews-feedback success" role="status">
              Your review has been posted.
            </p>
          )}
          <button className="button" type="submit" disabled={submitting}>
            {submitting ? 'Posting review…' : 'Post review'}
          </button>
        </form>
      ) : user ? (
        <p className="review-form-note">Only customer accounts can post reviews.</p>
      ) : (
        <p className="review-form-note">
          <Link to={loginUrl}>Log in to write a review</Link>, or{' '}
          <Link to="/register">create an account</Link>.
        </p>
      )}
    </details>
  )
}
