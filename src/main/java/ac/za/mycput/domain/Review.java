
package ac.za.mycput.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 240)
    private String comment;

    @Column(nullable = false)
    private LocalDate reviewDate;

    @Column(nullable = false)
    private boolean verifiedPurchase = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewStatus status = ReviewStatus.APPROVED;

    @Column(nullable = false)
    private int helpfulCount = 0;

    @Column(length = 1000)
    private String adminResponse;

    private LocalDateTime responseDate;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Review() {
    }

    public Review(Builder builder) {
        this.reviewId = builder.reviewId;
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.reviewDate = builder.reviewDate;
        this.verifiedPurchase = builder.verifiedPurchase;
        this.status = builder.status;
        this.helpfulCount = builder.helpfulCount;
        this.adminResponse = builder.adminResponse;
        this.responseDate = builder.responseDate;
        this.updatedAt = builder.updatedAt;
        this.product = builder.product;
        this.customer = builder.customer;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public boolean isVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static class Builder {
        private Long reviewId;
        private int rating;
        private String comment;
        private LocalDate reviewDate;
        private boolean verifiedPurchase = false;
        private ReviewStatus status = ReviewStatus.APPROVED;
        private int helpfulCount = 0;
        private String adminResponse;
        private LocalDateTime responseDate;
        private LocalDateTime updatedAt;
        private Product product;
        private Customer customer;

        public Builder setReviewId(Long reviewId) {
            this.reviewId = reviewId;
            return this;
        }

        public Builder setRating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setReviewDate(LocalDate reviewDate) {
            this.reviewDate = reviewDate;
            return this;
        }

        public Builder setVerifiedPurchase(boolean verifiedPurchase) {
            this.verifiedPurchase = verifiedPurchase;
            return this;
        }

        public Builder setStatus(ReviewStatus status) {
            this.status = status;
            return this;
        }

        public Builder setHelpfulCount(int helpfulCount) {
            this.helpfulCount = helpfulCount;
            return this;
        }

        public Builder setAdminResponse(String adminResponse) {
            this.adminResponse = adminResponse;
            return this;
        }

        public Builder setResponseDate(LocalDateTime responseDate) {
            this.responseDate = responseDate;
            return this;
        }

        public Builder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder copy(Review review) {
            this.reviewId = review.reviewId;
            this.rating = review.rating;
            this.comment = review.comment;
            this.reviewDate = review.reviewDate;
            this.verifiedPurchase = review.verifiedPurchase;
            this.status = review.status;
            this.helpfulCount = review.helpfulCount;
            this.adminResponse = review.adminResponse;
            this.responseDate = review.responseDate;
            this.updatedAt = review.updatedAt;
            this.product = review.product;
            this.customer = review.customer;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }
}
