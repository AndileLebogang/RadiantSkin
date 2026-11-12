/*
    ReviewHelpfulVoteRepository.java

    Review Helpful Vote Repository interface

    Author: La-eeq Lewis (240696255)

    Date: 11 November 2026
*/

package ac.za.mycput.repository;

import ac.za.mycput.domain.ReviewHelpfulVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewHelpfulVoteRepository
        extends JpaRepository<ReviewHelpfulVote, Long> {

    boolean existsByReview_ReviewIdAndCustomer_UserId(
            Long reviewId,
            Long customerId
    );

    Optional<ReviewHelpfulVote> findByReview_ReviewIdAndCustomer_UserId(
            Long reviewId,
            Long customerId
    );

    long countByReview_ReviewId(Long reviewId);

    void deleteByReview_ReviewId(Long reviewId);
}
