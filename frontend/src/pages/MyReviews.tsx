import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { FiArrowLeft, FiArrowRight, FiEdit2, FiStar, FiTrash2 } from 'react-icons/fi'
import { useAuth } from '../AuthContext'
import { useStore } from '../StoreContext'
import { ApiError, createApi } from '../lib/store'
import type { Review } from '../lib/store'

const api = createApi(import.meta.env.VITE_API_BASE_URL || '/api')

export default function MyReviewsPage() {
  const { user, loading: accountLoading } = useAuth()
  const { products, loading: productsLoading } = useStore()

  const [reviews, setReviews] = useState<Review[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [editingId, setEditingId] = useState<number | null>(null)
  const [editRating, setEditRating] = useState(5)
  const [editComment, setEditComment] = useState('')
  const [saving, setSaving] = useState(false)
  const [deletingId, setDeletingId] = useState<number | null>(null)

  useEffect(() => {
    if (!user) return

    let active = true

    async function loadReviews() {
      setLoading(true)
      setError('')

      try {
        const result = await api.myReviews()
        if (active) setReviews(result)
      } catch (error) {
        if (active) {
          setError(error instanceof Error ? error.message : 'Your reviews could not be loaded.')
        }
      } finally {
        if (active) setLoading(false)
      }
    }

    loadReviews()

    return () => {
      active = false
    }
  }, [user])

  if (accountLoading) {
    return (
      <section className="page section" role="status">
        Loading your account...
      </section>
    )
  }

  if (!user) {
    return <Navigate to="/login?next=/my-reviews" replace />
  }

  function startEditing(review: Review) {
    setEditingId(review.reviewId)
    setEditRating(review.rating)
    setEditComment(review.comment)
    setError('')
  }

  function cancelEditing() {
    setEditingId(null)
    setEditRating(5)
    setEditComment('')
  }

  async function saveReview(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (editingId === null || saving) return

    const comment = editComment.trim()

    if (!comment) {
      setError('Please enter a comment for your review.')
      return
    }

    setSaving(true)
    setError('')

    try {
      const updated = await api.updateReview(editingId, {
        rating: editRating,
        comment,
      })

      setReviews((current) =>
        current.map((review) => (review.reviewId === editingId ? updated : review)),
      )

      cancelEditing()
    } catch (error) {
      setError(
        error instanceof ApiError
          ? error.message
          : error instanceof Error
            ? error.message
            : 'Your review could not be updated.',
      )
    } finally {
      setSaving(false)
    }
  }

  async function removeReview(reviewId: number) {
    if (deletingId !== null) return

    const confirmed = window.confirm(
      'Are you sure you want to delete this review? This action cannot be undone.',
    )

    if (!confirmed) return

    setDeletingId(reviewId)
    setError('')

    try {
      await api.deleteReview(reviewId)
      setReviews((current) => current.filter((review) => review.reviewId !== reviewId))

      if (editingId === reviewId) cancelEditing()
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Your review could not be deleted.')
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <section className="page section my-reviews-page">
      <div className="page-heading">
        <span className="eyebrow">Your RadiantSkin experience</span>
        <h1>My reviews.</h1>
        <p>Your thoughts, ratings and experiences with the products you have tried.</p>
      </div>

      <Link className="text-link" to="/account">
        <FiArrowLeft /> Back to your account
      </Link>

      {error && (
        <p className="error-message" role="alert">
          {error}
        </p>
      )}

      {loading || productsLoading ? (
        <p className="my-reviews-loading" role="status">
          Loading your reviews...
        </p>
      ) : reviews.length === 0 ? (
        <div className="my-reviews-empty">
          <span className="eyebrow">Your review history</span>
          <h2>No reviews yet.</h2>
          <p>Once you have reviewed a product, you will find your reviews here.</p>
          <Link className="button" to="/shop">
            Explore the collection <FiArrowRight />
          </Link>
        </div>
      ) : (
        <div className="my-reviews-list">
          <div className="my-reviews-summary">
            <span className="eyebrow">Your contributions</span>
            <h2>
              {reviews.length} {reviews.length === 1 ? 'review' : 'reviews'}
            </h2>
          </div>

          {reviews.map((review) => {
            const product = products.find((item) => item.productId === review.productId)
            const isEditing = editingId === review.reviewId
            const reviewDate = new Date(review.reviewDate)

            return (
              <article className="my-review-card" key={review.reviewId}>
                <div className="my-review-header">
                  <div>
                    <span className="eyebrow">Product review</span>
                    <h2>
                      {product ? (
                        <Link to={`/products/${product.productId}`}>{product.name}</Link>
                      ) : (
                        'Product'
                      )}
                    </h2>
                  </div>

                  {product && (
                    <Link className="text-link" to={`/products/${product.productId}`}>
                      View product <FiArrowRight />
                    </Link>
                  )}
                </div>

                <div className="my-review-meta">
                  <div className="my-review-stars" aria-label={`${review.rating} out of 5 stars`}>
                    {Array.from({ length: 5 }, (_, index) => (
                      <FiStar
                        key={index}
                        aria-hidden="true"
                        className={index < review.rating ? 'filled' : ''}
                      />
                    ))}
                  </div>

                  {review.verifiedPurchase && (
                    <span className="verified-purchase-badge">
                      <span aria-hidden="true">✓</span>
                      Verified Purchase
                    </span>
                  )}

                  <span className="my-review-date">
                    {Number.isNaN(reviewDate.getTime())
                      ? review.reviewDate
                      : reviewDate.toLocaleDateString('en-ZA', {
                          day: 'numeric',
                          month: 'long',
                          year: 'numeric',
                        })}
                  </span>
                </div>

                {isEditing ? (
                  <form className="my-review-edit-form" onSubmit={saveReview}>
                    <label>Your rating</label>

                    <div className="my-review-rating-picker">
                      {Array.from({ length: 5 }, (_, index) => {
                        const rating = index + 1

                        return (
                          <button
                            key={rating}
                            type="button"
                            className={rating <= editRating ? 'selected' : ''}
                            aria-label={`${rating} star${rating === 1 ? '' : 's'}`}
                            aria-pressed={editRating === rating}
                            onClick={() => setEditRating(rating)}
                          >
                            <FiStar />
                          </button>
                        )
                      })}
                    </div>

                    <label htmlFor={`edit-review-${review.reviewId}`}>Your review</label>
                    <textarea
                      id={`edit-review-${review.reviewId}`}
                      value={editComment}
                      onChange={(event) => setEditComment(event.target.value)}
                      rows={4}
                      maxLength={240}
                      required
                    />
                    <span className="my-review-character-count">
                      {editComment.length}/240 characters
                    </span>

                    <div className="my-review-actions">
                      <button className="button" type="submit" disabled={saving}>
                        {saving ? 'Saving...' : 'Save changes'}
                      </button>
                      <button
                        className="text-link"
                        type="button"
                        disabled={saving}
                        onClick={cancelEditing}
                      >
                        Cancel
                      </button>
                    </div>
                  </form>
                ) : (
                  <>
                    <p className="my-review-comment">{review.comment}</p>

                    <div className="my-review-footer">
                      <span>
                        <FiStar aria-hidden="true" />
                        {review.helpfulCount}{' '}
                        {review.helpfulCount === 1
                          ? 'person found this helpful'
                          : 'people found this helpful'}
                      </span>

                      <div className="my-review-actions">
                        <button
                          className="text-link"
                          type="button"
                          onClick={() => startEditing(review)}
                        >
                          <FiEdit2 /> Edit
                        </button>

                        <button
                          className="text-link my-review-delete"
                          type="button"
                          disabled={deletingId === review.reviewId}
                          onClick={() => removeReview(review.reviewId)}
                        >
                          <FiTrash2 />
                          {deletingId === review.reviewId ? 'Deleting...' : 'Delete'}
                        </button>
                      </div>
                    </div>
                  </>
                )}
              </article>
            )
          })}
        </div>
      )}
    </section>
  )
}
