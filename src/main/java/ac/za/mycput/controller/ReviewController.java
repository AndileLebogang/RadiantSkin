package ac.za.mycput.controller;

/*
// Name : Siphokazi Malingatshoni
// Student no :222868708
 */

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.ReviewStatus;
import ac.za.mycput.domain.Role;
import ac.za.mycput.domain.User;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.IReviewService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final IReviewService service;
    private final AccountSession sessions;

    public ReviewController(IReviewService service, AccountSession sessions) {
        this.service = service;
        this.sessions = sessions;
    }

    // --------------------------------------------------
    // Request and response DTOs
    // --------------------------------------------------

    public record ReviewSubmission(
            Long productId,
            Integer rating,
            String comment) {}

    public record ReviewUpdate(
            Integer rating,
            String comment) {}

    public record ModerationRequest(
            ReviewStatus status) {}

    public record ReviewView(
            Long reviewId,
            Long productId,
            int rating,
            String comment,
            LocalDate reviewDate,
            boolean verifiedPurchase,
            int helpfulCount,
            LocalDateTime updatedAt) {

        public static ReviewView from(Review review) {
            return new ReviewView(
                    review.getReviewId(),
                    review.getProduct().getProductId(),
                    review.getRating(),
                    review.getComment(),
                    review.getReviewDate(),
                    review.isVerifiedPurchase(),
                    review.getHelpfulCount(),
                    review.getUpdatedAt()
            );
        }
    }

    public record ReviewStatistics(
            Double averageRating,
            long totalReviews,
            List<RatingCount> distribution) {}

    public record RatingCount(int rating, long count) {}

    public record PageResponse<T>(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last) {}

    // --------------------------------------------------
    // Authentication helpers
    // --------------------------------------------------

    private Customer currentCustomer(HttpServletRequest request) {
        return sessions.customer(request);
    }

    private boolean isAdmin(HttpServletRequest request) {
        User user = sessions.current(request);
        return user != null && user.getRole() == Role.ADMIN;
    }

    // --------------------------------------------------
    // Public review retrieval
    // --------------------------------------------------

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0 || size < 1 || size > 50) {
            return ResponseEntity.badRequest()
                    .body("Invalid pagination values");
        }

        try {
            Pageable pageable = PageRequest.of(page, size);

            Page<Review> reviews = service.getApprovedReviews(
                    productId, sort, pageable);

            PageResponse<ReviewView> response = new PageResponse<>(
                    reviews.getContent().stream()
                            .map(ReviewView::from)
                            .toList(),
                    reviews.getNumber(),
                    reviews.getSize(),
                    reviews.getTotalElements(),
                    reviews.getTotalPages(),
                    reviews.isLast()
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

  
@GetMapping("/product/{productId}/statistics")
public ResponseEntity<?> getStatistics(
        @PathVariable Long productId) {

    try {
        Double average = service.getAverageRating(productId);
        long total = service.getReviewCount(productId);

        List<RatingCount> distribution =
                service.getRatingDistribution(productId)
                        .stream()
                        .map(row -> new RatingCount(
                                ((Number) row[0]).intValue(),
                                ((Number) row[1]).longValue()))
                        .toList();

        return ResponseEntity.ok(new ReviewStatistics(
                average,
                total,
                distribution
        ));

    } catch (IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }
}

    // --------------------------------------------------
    // Customer review submission
    // --------------------------------------------------

    @PostMapping("/submit")
    public ResponseEntity<?> submit(
            @RequestBody ReviewSubmission submission,
            HttpServletRequest request) {

        Customer customer = currentCustomer(request);

        if (customer == null) {
            User user = sessions.current(request);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Log in to post a review.");
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only customers can post reviews.");
        }

        if (submission == null
                || submission.productId() == null
                || submission.rating() == null) {

            return ResponseEntity.badRequest()
                    .body("Product and rating are required.");
        }

        try {
            Review saved = service.addReview(
                    customer.getUserId(),
                    submission.productId(),
                    submission.rating(),
                    submission.comment()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ReviewView.from(saved));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    // --------------------------------------------------
    // Customer review management
    // --------------------------------------------------

    @GetMapping("/my-reviews")
    public ResponseEntity<?> getMyReviews(
            HttpServletRequest request) {

        Customer customer = currentCustomer(request);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Customer login required.");
        }

        List<ReviewView> reviews = service
                .findByCustomerId(customer.getUserId())
                .stream()
                .map(ReviewView::from)
                .toList();

        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/my-reviews/{reviewId}")
    public ResponseEntity<?> updateMyReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdate update,
            HttpServletRequest request) {

        Customer customer = currentCustomer(request);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Customer login required.");
        }

        if (update == null || update.rating() == null) {
            return ResponseEntity.badRequest()
                    .body("Rating is required.");
        }

        try {
            Review updated = service.updateCustomerReview(
                    reviewId,
                    customer.getUserId(),
                    update.rating(),
                    update.comment()
            );

            return ResponseEntity.ok(ReviewView.from(updated));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    @DeleteMapping("/my-reviews/{reviewId}")
    public ResponseEntity<?> deleteMyReview(
            @PathVariable Long reviewId,
            HttpServletRequest request) {

        Customer customer = currentCustomer(request);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Customer login required.");
        }

        try {
            service.deleteCustomerReview(
                    reviewId, customer.getUserId());

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    // --------------------------------------------------
    // Admin moderation
    // --------------------------------------------------

    @GetMapping("/admin/moderation")
    public ResponseEntity<?> getReviewsForModeration(
            @RequestParam(defaultValue = "HIDDEN") ReviewStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Administrator access required.");
        }

        if (page < 0 || size < 1 || size > 50) {
            return ResponseEntity.badRequest()
                    .body("Invalid pagination values.");
        }

        Page<Review> reviews = service.getReviewsForModeration(
                status, PageRequest.of(page, size));

        return ResponseEntity.ok(new PageResponse<>(
                reviews.getContent().stream()
                        .map(ReviewView::from)
                        .toList(),
                reviews.getNumber(),
                reviews.getSize(),
                reviews.getTotalElements(),
                reviews.getTotalPages(),
                reviews.isLast()
        ));
    }

    @PutMapping("/admin/{reviewId}/status")
    public ResponseEntity<?> moderateReview(
            @PathVariable Long reviewId,
            @RequestBody ModerationRequest moderation,
            HttpServletRequest request) {

        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Administrator access required.");
        }

        if (moderation == null || moderation.status() == null) {
            return ResponseEntity.badRequest()
                    .body("Review status is required.");
        }

        try {
            Review updated = service.updateReviewStatus(
                    reviewId, moderation.status());

            return ResponseEntity.ok(ReviewView.from(updated));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }
}