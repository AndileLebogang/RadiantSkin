/*
    ReviewHelpfulVote.java

    Review Helpful Vote POJO class

    Author: La-eeq Lewis (240696255)

    Date: 11 November 2026
*/

package ac.za.mycput.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "review_helpful_vote",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_review_helpful_customer",
            columnNames = {"review_id", "customer_id"}
        )
    },
    indexes = {
        @Index(name = "idx_helpful_vote_review", columnList = "review_id"),
        @Index(name = "idx_helpful_vote_customer", columnList = "customer_id")
    }
)
public class ReviewHelpfulVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ReviewHelpfulVote() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getVoteId() {
        return voteId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

