package ac.za.mycput.service;
/*
/Name: Siphokazi Malingatshoni
/Student Number: 222868708
 */

import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.ReviewStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReviewService extends IService<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByCustomerId(Long customerId);

    Review addReview(Long customerId, Long productId, int rating, String comment);

    // Public review retrieval
    Page<Review> getApprovedReviews(Long productId, String sort, Pageable pageable);

    // Review statistics
    Double getAverageRating(Long productId);

    long getReviewCount(Long productId);

    List<Object[]> getRatingDistribution(Long productId);

    // Customer review management
    Review updateCustomerReview(Long reviewId, Long customerId, int rating, String comment);

    boolean deleteCustomerReview(Long reviewId, Long customerId);

    // Verified purchase
    boolean hasVerifiedPurchase(Long customerId, Long productId);

    // Admin moderation
    Page<Review> getReviewsForModeration(ReviewStatus status, Pageable pageable);

    Review updateReviewStatus(Long reviewId, ReviewStatus status);
}