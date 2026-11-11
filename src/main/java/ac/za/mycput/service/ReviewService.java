
package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.Product;
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.ReviewStatus;

import ac.za.mycput.repository.CustomerRepository;
import ac.za.mycput.repository.ProductRepository;
import ac.za.mycput.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReviewService implements IReviewService {

    private final ReviewRepository repository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(
            ReviewRepository repository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.repository = repository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // --------------------------------------------------
    // Standard CRUD methods
    // --------------------------------------------------

    @Override
    public Review create(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be empty");
        }

        validateReview(review.getRating(), review.getComment());

        return repository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Review read(Long id) {
        if (id == null) {
            return null;
        }

        return repository.findById(id).orElse(null);
    }

    @Override
    public Review update(Review review) {
        if (review == null || review.getReviewId() == null) {
            throw new IllegalArgumentException("Valid review is required");
        }

        Review existing = repository.findById(review.getReviewId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Review not found"));

        validateReview(review.getRating(), review.getComment());

        // Only update editable review content.
        // Do not replace the customer, product or verification status.
        existing.setRating(review.getRating());
        existing.setComment(review.getComment().trim());
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (id == null || !repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAll() {
        return repository.findAll();
    }

    // --------------------------------------------------
    // Find reviews
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<Review> findByProductId(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        // Only approved reviews are returned for public display.
        return repository.findByProduct(product)
                .stream()
                .filter(review -> review.getStatus() == ReviewStatus.APPROVED)
                .toList();
    }

    @Override
@Transactional(readOnly = true)
public List<Review> findByCustomerId(Long customerId) {

    if (customerId == null) {
        throw new IllegalArgumentException("Customer ID is required");
    }

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found"));

    // Return all of this customer's reviews, including hidden ones.
    return repository.findByCustomer(customer);
}

    // --------------------------------------------------
    // Submit a review
    // --------------------------------------------------

    @Override
    public Review addReview(
            Long customerId,
            Long productId,
            int rating,
            String comment) {

        if (customerId == null || productId == null) {
            throw new IllegalArgumentException(
                    "Customer and product are required");
        }

        validateReview(rating, comment);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        // One review per customer per product.
        if (repository.findByCustomerUserIdAndProductProductId(
                customerId, productId).isPresent()) {

            throw new IllegalArgumentException(
                    "You have already reviewed this product");
        }

        boolean verified = hasVerifiedPurchase(customerId, productId);

        Review review = new Review.Builder()
                .setCustomer(customer)
                .setProduct(product)
                .setRating(rating)
                .setComment(comment.trim())
                .setReviewDate(LocalDate.now())
                .setVerifiedPurchase(verified)
                .setStatus(ReviewStatus.APPROVED)
                .setHelpfulCount(0)
                .build();

        return repository.save(review);
    }

    // --------------------------------------------------
    // Public reviews with sorting and pagination
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public Page<Review> getApprovedReviews(
            Long productId,
            String sort,
            Pageable pageable) {

        if (productId == null || !productRepository.existsById(productId)) {
    throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Product not found");
}

        if (sort == null) {
            sort = "recent";
        }

        return switch (sort.toLowerCase()) {
            case "highest" ->
                    repository.findByProductProductIdAndStatusOrderByRatingDesc(
                            productId, ReviewStatus.APPROVED, pageable);

            case "lowest" ->
                    repository.findByProductProductIdAndStatusOrderByRatingAsc(
                            productId, ReviewStatus.APPROVED, pageable);

            case "helpful" ->
                    repository.findByProductProductIdAndStatusOrderByHelpfulCountDesc(
                            productId, ReviewStatus.APPROVED, pageable);

            case "recent" ->
                    repository.findByProductProductIdAndStatusOrderByReviewDateDesc(
                            productId, ReviewStatus.APPROVED, pageable);

            default -> throw new IllegalArgumentException(
                    "Unsupported review sort option");
        };
    }

    // --------------------------------------------------
    // Review statistics
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(Long productId) {

        if (productId == null || !productRepository.existsById(productId)) {
    throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Product not found");
}

        Double average = repository.findAverageRating(
                productId, ReviewStatus.APPROVED);

        return average == null ? 0.0 : average;
    }

    @Override
@Transactional(readOnly = true)
public long getReviewCount(Long productId) {

    if (productId == null || !productRepository.existsById(productId)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found");
    }

    return repository.countByProductProductIdAndStatus(
            productId, ReviewStatus.APPROVED);
}

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getRatingDistribution(Long productId) {

        if (productId == null || !productRepository.existsById(productId)) {
    throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Product not found");
}

        return repository.findRatingDistribution(
                productId, ReviewStatus.APPROVED);
    }

    // --------------------------------------------------
    // Customer review editing
    // --------------------------------------------------

    @Override
public Review updateCustomerReview(
        Long reviewId,
        Long customerId,
        int rating,
        String comment) {

    if (reviewId == null || customerId == null) {
        throw new IllegalArgumentException(
                "Review ID and customer ID are required");
    }

    validateReview(rating, comment);

    Review review = repository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Review not found"));

    if (review.getCustomer() == null
            || !customerId.equals(review.getCustomer().getUserId())) {

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You cannot edit another customer's review");
    }

    review.setRating(rating);
    review.setComment(comment.trim());
    review.setUpdatedAt(LocalDateTime.now());

    return repository.save(review);
}

    // --------------------------------------------------
    // Customer review deletion
    // --------------------------------------------------

    @Override
public boolean deleteCustomerReview(Long reviewId, Long customerId) {

    if (reviewId == null || customerId == null) {
        throw new IllegalArgumentException(
                "Review ID and customer ID are required");
    }

    Review review = repository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Review not found"));

    if (review.getCustomer() == null
            || !customerId.equals(review.getCustomer().getUserId())) {

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You cannot delete another customer's review");
    }

    repository.delete(review);
    return true;
}

    // --------------------------------------------------
    // Verified purchase checking
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public boolean hasVerifiedPurchase(
            Long customerId,
            Long productId) {

        List<OrderStatus> qualifyingStatuses = List.of(
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED
        );

        return repository.hasCustomerPurchasedProduct(
                customerId, productId, qualifyingStatuses);
    }

    // --------------------------------------------------
    // Admin moderation
    // --------------------------------------------------

    @Override
@Transactional(readOnly = true)
public Page<Review> getReviewsForModeration(
        ReviewStatus status,
        Pageable pageable) {

    if (status == null || pageable == null) {
        throw new IllegalArgumentException(
                "Review status and pagination are required");
    }

    return repository.findByStatus(status, pageable);
}

    @Override
public Review updateReviewStatus(
        Long reviewId,
        ReviewStatus status) {

    if (reviewId == null || status == null) {
        throw new IllegalArgumentException(
                "Review ID and status are required");
    }

    Review review = repository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Review not found"));

    review.setStatus(status);

    return repository.save(review);
}

    // --------------------------------------------------
    // Shared validation
    // --------------------------------------------------

    private void validateReview(int rating, String comment) {

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5");
        }

        String trimmedComment = comment == null ? "" : comment.trim();

        if (trimmedComment.length() < 3
                || trimmedComment.length() > 240) {

            throw new IllegalArgumentException(
                    "Review must be between 3 and 240 characters");
        }
    }
}

