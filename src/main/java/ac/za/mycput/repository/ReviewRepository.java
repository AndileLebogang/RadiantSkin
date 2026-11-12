
package ac.za.mycput.repository;

import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.ReviewStatus;
import ac.za.mycput.domain.Product;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find all reviews for a product
    List<Review> findByProduct(Product product);

    // Find all reviews submitted by a customer
    List<Review> findByCustomer(Customer customer);

    // Public reviews: approved reviews only
    Page<Review> findByProductProductIdAndStatus(
            Long productId,
            ReviewStatus status,
            Pageable pageable
    );

    // Approved reviews sorted by rating
    Page<Review> findByProductProductIdAndStatusOrderByRatingDesc(
            Long productId,
            ReviewStatus status,
            Pageable pageable
    );

    Page<Review> findByProductProductIdAndStatusOrderByRatingAsc(
            Long productId,
            ReviewStatus status,
            Pageable pageable
    );

    // Reviews submitted by a particular customer
    Page<Review> findByCustomerUserId(
            Long customerId,
            Pageable pageable
    );

    // Average rating for a product
    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.product.productId = :productId
        AND r.status = :status
    """)
    Double findAverageRating(
            @Param("productId") Long productId,
            @Param("status") ReviewStatus status
    );

    // Total number of approved reviews
    long countByProductProductIdAndStatus(
            Long productId,
            ReviewStatus status
    );

    // Number of reviews for each star rating
    @Query("""
        SELECT r.rating, COUNT(r)
        FROM Review r
        WHERE r.product.productId = :productId
        AND r.status = :status
        GROUP BY r.rating
        ORDER BY r.rating DESC
    """)
    List<Object[]> findRatingDistribution(
            @Param("productId") Long productId,
            @Param("status") ReviewStatus status
    );

    // Find a customer's existing review of a product
    Optional<Review> findByCustomerUserIdAndProductProductId(
            Long customerId,
            Long productId
    );

    // Check whether a customer has purchased a specific  product
    @Query("""
        SELECT COUNT(oi) > 0
        FROM OrderItem oi
        JOIN oi.order o
        WHERE o.customer.userId = :customerId
        AND oi.product.productId = :productId
        AND o.status IN :qualifyingStatuses
    """)
    boolean hasCustomerPurchasedProduct(
            @Param("customerId") Long customerId,
            @Param("productId") Long productId,
            @Param("qualifyingStatuses") List<OrderStatus> qualifyingStatuses
    );

    // Retrieve reviews for admin moderation
    Page<Review> findByStatus(
            ReviewStatus status,
            Pageable pageable
    );

    // Retrieve reviews in descending order of submission date
   Page<Review> findByProductProductIdAndStatusOrderByReviewDateDesc(
        Long productId,
        ReviewStatus status,
        Pageable pageable
    );

    // Retrieve reviews that is most helpful (highest helpfulCount) for a product
    Page<Review> findByProductProductIdAndStatusOrderByHelpfulCountDesc(
        Long productId,
        ReviewStatus status,
        Pageable pageable
    );
    
}
