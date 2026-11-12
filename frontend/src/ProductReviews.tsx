import { useEffect, useMemo, useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { FiStar } from 'react-icons/fi'
import { ApiError, createApi } from './lib/store'
import type { Review, ReviewStatistics } from './lib/store'
import { useAuth } from './AuthContext'

const api = createApi(import.meta.env.VITE_API_BASE_URL || '/api')

export default function ProductReviews({ productId }: { productId: number }) {
  const { user, loading: accountLoading, sessionError, refresh } = useAuth()
  const loginUrl = `/login?next=${encodeURIComponent(`/products/${productId}`)}`

  const [statistics, setStatistics] = useState<ReviewStatistics | null>(null)
  const [statisticsError, setStatisticsError] = useState('')
  const [reviews, setReviews] = useState<Review[]>([])
  const [customerReviewIds, setCustomerReviewIds] = useState<Set<number>>(new Set())
  const [helpfulVotes, setHelpfulVotes] = useState<Record<number, boolean>>({})
  const [votingReviewIds, setVotingReviewIds] = useState<Set<number>>(new Set())

  const [loading, setLoading] = useState(true)
  const [loadingMore, setLoadingMore] = useState(false)
  const [loadError, setLoadError] = useState('')
  const [retry, setRetry] = useState(0)

  const [sortBy, setSortBy] = useState<'recent' | 'highest' | 'lowest' | 'helpful'>('recent')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState('')
  const [submitted, setSubmitted] = useState(false)

  const [editingReviewId, setEditingReviewId] = useState<number | null>(null)
  const [editRating, setEditRating] = useState(5)
  const [editComment, setEditComment] = useState('')
  const [savingEdit, setSavingEdit] = useState(false)
  const [deletingReviewId, setDeletingReviewId] = useState<number | null>(null)
  const [actionError, setActionError] = useState('')

  const formRef = useRef<HTMLFormElement>(null)

  const ownReviewIds = useMemo(
    () => (user?.role === 'CUSTOMER' ? customerReviewIds : new Set<number>()),
    [customerReviewIds, user?.role],
  )

  const reviewIdKey = reviews.map((review) => review.reviewId).join(',')

  // Load public reviews with sorting and pagination.
  useEffect(() => {
    const controller = new AbortController()

    queueMicrotask(() => {
      if (controller.signal.aborted) {
        return
      }

      if (page === 0) {
        setLoading(true)
        setLoadError('')
      } else {
        setLoadingMore(true)
      }
    })

    api
      .reviews(productId, controller.signal, sortBy, page, 5)
      .then((result) => {
        if (!controller.signal.aborted) {
          setReviews((current) =>
            page === 0
              ? result.content
              : [
                  ...current,
                  ...result.content.filter(
                    (newReview) =>
                      !current.some((existing) => existing.reviewId === newReview.reviewId),
                  ),
                ],
          )

          setTotalPages(result.totalPages)
          setLoadError('')
        }
      })
      .catch((error) => {
        if (!controller.signal.aborted) {
          setLoadError(error instanceof Error ? error.message : 'Reviews could not be loaded.')
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) {
          setLoading(false)
          setLoadingMore(false)
        }
      })

    return () => controller.abort()
  }, [productId, retry, sortBy, page])

  // Load average rating and rating distribution.
  useEffect(() => {
    let active = true

    api
      .reviewStatistics(productId)
      .then((result) => {
        if (active) {
          setStatistics(result)
          setStatisticsError('')
        }
      })
      .catch((error) => {
        if (active) {
          setStatisticsError(
            error instanceof Error ? error.message : 'Review statistics could not be loaded.',
          )
        }
      })

    return () => {
      active = false
    }
  }, [productId, retry])

  // Identify reviews belonging to the logged-in customer.
  useEffect(() => {
    let active = true

    if (accountLoading || user?.role !== 'CUSTOMER') {
      return () => {
        active = false
      }
    }

    api
      .myReviews()
      .then((customerReviews) => {
        if (active) {
          setCustomerReviewIds(
            new Set(
              customerReviews
                .filter((review) => review.productId === productId)
                .map((review) => review.reviewId),
            ),
          )
        }
      })
      .catch((error) => {
        if (active) {
          setActionError(
            error instanceof Error ? error.message : 'Your reviews could not be loaded.',
          )
        }
      })

    return () => {
      active = false
    }
  }, [user, accountLoading, productId, retry])

  // Load the logged-in customer's helpful-vote status.
  useEffect(() => {
    let active = true

    if (accountLoading || user?.role !== 'CUSTOMER' || reviews.length === 0) {
      return () => {
        active = false
      }
    }

    Promise.all(
      reviews.map(async (review) => {
        try {
          const result = await api.helpfulVoteStatus(review.reviewId)

          return {
            reviewId: review.reviewId,
            voted: result.voted,
            helpfulCount: result.helpfulCount,
          }
        } catch {
          return null
        }
      }),
    ).then((results) => {
      if (!active) return

      const validResults = results.filter(
        (result): result is NonNullable<typeof result> => result !== null,
      )

      setHelpfulVotes((current) => {
        const updated = { ...current }

        validResults.forEach((result) => {
          updated[result.reviewId] = result.voted
        })

        return updated
      })

      setReviews((current) =>
        current.map((review) => {
          const result = validResults.find((item) => item.reviewId === review.reviewId)

          return result ? { ...review, helpfulCount: result.helpfulCount } : review
        }),
      )
    })

    return () => {
      active = false
    }
  }, [reviewIdKey, accountLoading, user?.role])

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
      setCustomerReviewIds((current) => new Set([...current, saved.reviewId]))

      formRef.current?.reset()
      setSubmitted(true)
    } catch (error) {
      setSubmitError(error instanceof Error ? error.message : 'Your review could not be sent.')

      if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
        await refresh()
      }
    } finally {
      setSubmitting(false)
    }
  }

  function startEditing(review: Review) {
    setEditingReviewId(review.reviewId)
    setEditRating(review.rating)
    setEditComment(review.comment)
    setActionError('')
  }

  async function saveEdit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (editingReviewId === null || savingEdit) return

    setSavingEdit(true)
    setActionError('')

    try {
      const updated = await api.updateReview(editingReviewId, {
        rating: editRating,
        comment: editComment.trim(),
      })

      setReviews((current) =>
        current.map((review) => (review.reviewId === updated.reviewId ? updated : review)),
      )

      setEditingReviewId(null)
      setEditComment('')
    } catch (error) {
      setActionError(error instanceof Error ? error.message : 'Your review could not be updated.')

      if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
        await refresh()
      }
    } finally {
      setSavingEdit(false)
    }
  }

  async function deleteReview(reviewId: number) {
    if (deletingReviewId !== null) return

    const confirmed = window.confirm(
      'Are you sure you want to delete your review? This cannot be undone.',
    )

    if (!confirmed) return

    setDeletingReviewId(reviewId)
    setActionError('')

    try {
      await api.deleteReview(reviewId)

      setReviews((current) => current.filter((review) => review.reviewId !== reviewId))

      setCustomerReviewIds((current) => {
        const next = new Set(current)
        next.delete(reviewId)
        return next
      })

      if (editingReviewId === reviewId) {
        setEditingReviewId(null)
      }
    } catch (error) {
      setActionError(error instanceof Error ? error.message : 'Your review could not be deleted.')

      if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
        await refresh()
      }
    } finally {
      setDeletingReviewId(null)
    }
  }

  async function toggleHelpfulVote(reviewId: number) {
    if (user?.role !== 'CUSTOMER' || ownReviewIds.has(reviewId) || votingReviewIds.has(reviewId)) {
      return
    }

    setVotingReviewIds((current) => new Set([...current, reviewId]))
    setActionError('')

    try {
      const alreadyVoted = helpfulVotes[reviewId]

      const result = alreadyVoted
        ? await api.removeHelpfulVote(reviewId)
        : await api.addHelpfulVote(reviewId)

      setHelpfulVotes((current) => ({
        ...current,
        [reviewId]: result.voted,
      }))

      setReviews((current) =>
        current.map((review) =>
          review.reviewId === reviewId ? { ...review, helpfulCount: result.helpfulCount } : review,
        ),
      )
    } catch (error) {
      setActionError(
        error instanceof Error ? error.message : 'Your helpful vote could not be saved.',
      )

      if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
        await refresh()
      }
    } finally {
      setVotingReviewIds((current) => {
        const next = new Set(current)
        next.delete(reviewId)
        return next
      })
    }
  }

  return (
    <details className="product-reviews" open>
      <summary>Reviews ({reviews.length})</summary>

      <div className="review-controls">
        <label htmlFor="review-sort">Sort reviews:</label>

        <select
          id="review-sort"
          value={sortBy}
          onChange={(event) => {
            setReviews([])
            setPage(0)
            setSortBy(event.target.value as typeof sortBy)
          }}
        >
          <option value="recent">Most recent</option>
          <option value="highest">Highest rated</option>
          <option value="lowest">Lowest rated</option>
          <option value="helpful">Most helpful</option>
        </select>
      </div>

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

      {statisticsError && (
        <p className="reviews-feedback" role="alert">
          {statisticsError}
        </p>
      )}

      {statistics && (
        <div className="review-statistics">
          <h3>Customer ratings</h3>

          <div className="review-statistics-summary">
            <strong>{statistics.averageRating.toFixed(1)}</strong>

            <div>
              <div
                className="review-statistics-stars"
                aria-label={`${statistics.averageRating.toFixed(1)} out of 5 stars`}
              >
                {Array.from({ length: 5 }, (_, index) => {
                  const average = statistics.averageRating
                  const fullStars = Math.floor(average)
                  const hasHalfStar = average - fullStars >= 0.5

                  if (index < fullStars) {
                    return <FiStar key={index} className="star-filled" aria-hidden="true" />
                  }

                  if (index === fullStars && hasHalfStar) {
                    return (
                      <span className="star-half" key={index} aria-hidden="true">
                        <FiStar className="star-empty" />
                        <span className="star-half-overlay">
                          <FiStar className="star-filled" />
                        </span>
                      </span>
                    )
                  }

                  return <FiStar key={index} className="star-empty" aria-hidden="true" />
                })}
              </div>

              <p>
                Based on {statistics.totalReviews}{' '}
                {statistics.totalReviews === 1 ? 'review' : 'reviews'}
              </p>
            </div>
          </div>

          <div className="review-rating-breakdown">
            {[5, 4, 3, 2, 1].map((rating) => {
              const count =
                statistics.distribution.find((item) => item.rating === rating)?.count ?? 0

              const percentage =
                statistics.totalReviews > 0 ? (count / statistics.totalReviews) * 100 : 0

              return (
                <div className="review-rating-row" key={rating}>
                  <span>{rating} stars</span>

                  <div className="review-rating-bar">
                    <div style={{ width: `${percentage}%` }} />
                  </div>

                  <span>{count}</span>
                </div>
              )
            })}
          </div>
        </div>
      )}

      {!loading &&
        !loadError &&
        (reviews.length ? (
          <div className="review-list">
            {reviews.map((review) => (
              <div className="review" key={review.reviewId}>
                <span className="review-stars" aria-label={`${review.rating} out of 5 stars`}>
                  {review.verifiedPurchase && (
                    <span className="verified-purchase-badge">
                      <span aria-hidden="true">✓</span>
                      Verified Purchase
                    </span>
                  )}

                  {Array.from({ length: 5 }, (_, index) => (
                    <FiStar
                      key={index}
                      className={index < review.rating ? 'star-filled' : 'star-empty'}
                      aria-hidden="true"
                    />
                  ))}
                </span>

                {editingReviewId === review.reviewId ? (
                  <form className="review-form" onSubmit={saveEdit}>
                    <h3>Edit your review</h3>

                    <label htmlFor={`edit-rating-${review.reviewId}`}>Rating</label>
                    <select
                      id={`edit-rating-${review.reviewId}`}
                      value={editRating}
                      onChange={(event) => setEditRating(Number(event.target.value))}
                      required
                    >
                      {[5, 4, 3, 2, 1].map((rating) => (
                        <option key={rating} value={rating}>
                          {rating} {rating === 1 ? 'star' : 'stars'}
                        </option>
                      ))}
                    </select>

                    <label htmlFor={`edit-comment-${review.reviewId}`}>Your review</label>
                    <textarea
                      id={`edit-comment-${review.reviewId}`}
                      value={editComment}
                      onChange={(event) => setEditComment(event.target.value)}
                      minLength={3}
                      maxLength={240}
                      rows={4}
                      required
                    />

                    <button className="button" type="submit" disabled={savingEdit}>
                      {savingEdit ? 'Saving…' : 'Save changes'}
                    </button>

                    <button
                      className="text-link"
                      type="button"
                      onClick={() => setEditingReviewId(null)}
                      disabled={savingEdit}
                    >
                      Cancel
                    </button>
                  </form>
                ) : (
                  <>
                    <p>{review.comment}</p>

                    {user?.role === 'CUSTOMER' && !ownReviewIds.has(review.reviewId) && (
                      <button
                        className="text-link"
                        type="button"
                        onClick={() => void toggleHelpfulVote(review.reviewId)}
                        disabled={votingReviewIds.has(review.reviewId)}
                        aria-pressed={helpfulVotes[review.reviewId] === true}
                      >
                        {votingReviewIds.has(review.reviewId)
                          ? 'Saving…'
                          : helpfulVotes[review.reviewId]
                            ? `Helpful ✓ (${review.helpfulCount})`
                            : `Helpful (${review.helpfulCount})`}
                      </button>
                    )}

                    {user?.role !== 'CUSTOMER' && (
                      <p className="review-form-note">Helpful votes: {review.helpfulCount}</p>
                    )}

                    {ownReviewIds.has(review.reviewId) && (
                      <div className="review-actions">
                        <button
                          className="text-link"
                          type="button"
                          onClick={() => startEditing(review)}
                        >
                          Edit
                        </button>

                        <button
                          className="text-link"
                          type="button"
                          onClick={() => void deleteReview(review.reviewId)}
                          disabled={deletingReviewId === review.reviewId}
                        >
                          {deletingReviewId === review.reviewId ? 'Deleting…' : 'Delete'}
                        </button>
                      </div>
                    )}
                  </>
                )}
              </div>
            ))}
          </div>
        ) : (
          <p>No reviews yet. Be the first to share your experience.</p>
        ))}

      {!loading && !loadError && page + 1 < totalPages && (
        <div className="review-load-more">
          <button
            className="button"
            type="button"
            onClick={() => setPage((current) => current + 1)}
            disabled={loadingMore}
          >
            {loadingMore ? 'Loading more reviews…' : 'Load more reviews'}
          </button>
        </div>
      )}

      {actionError && (
        <p className="reviews-feedback" role="alert">
          {actionError}
        </p>
      )}

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
