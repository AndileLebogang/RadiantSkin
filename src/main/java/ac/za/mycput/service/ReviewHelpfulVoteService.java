/*
    ReviewHelpfulVoteService.java

    Review Helpful Vote Service class

    Author: La-eeq Lewis (240696255)

    Date: 11 November 2026
*/

package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.ReviewHelpfulVote;
import ac.za.mycput.repository.ReviewHelpfulVoteRepository;
import ac.za.mycput.repository.ReviewRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ReviewHelpfulVoteService {

    private final ReviewHelpfulVoteRepository helpfulVoteRepository;
    private final ReviewRepository reviewRepository;

    public ReviewHelpfulVoteService(
            ReviewHelpfulVoteRepository helpfulVoteRepository,
            ReviewRepository reviewRepository) {
        this.helpfulVoteRepository = helpfulVoteRepository;
        this.reviewRepository = reviewRepository;
    }

    public long addHelpfulVote(Long reviewId, Customer customer) {

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in as a customer to vote.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found."));

        if (review.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot vote on your own review.");
        }

        if (helpfulVoteRepository.existsByReview_ReviewIdAndCustomer_UserId(
                reviewId, customer.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You have already marked this review as helpful.");
        }

        ReviewHelpfulVote vote = new ReviewHelpfulVote();
        vote.setReview(review);
        vote.setCustomer(customer);

        helpfulVoteRepository.save(vote);

        long helpfulCount = helpfulVoteRepository.countByReview_ReviewId(reviewId);

        review.setHelpfulCount(Math.toIntExact(helpfulCount));
        reviewRepository.save(review);

        return helpfulCount;
    }

    public long removeHelpfulVote(Long reviewId, Customer customer) {

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in as a customer to remove your vote.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found."));

        helpfulVoteRepository
                .findByReview_ReviewIdAndCustomer_UserId(
                        reviewId, customer.getUserId())
                .ifPresent(helpfulVoteRepository::delete);

        long helpfulCount = helpfulVoteRepository.countByReview_ReviewId(reviewId);

        review.setHelpfulCount(Math.toIntExact(helpfulCount));
        reviewRepository.save(review);

        return helpfulCount;
    }

    @Transactional(readOnly = true)
    public boolean hasCustomerVoted(Long reviewId, Long customerId) {
        return helpfulVoteRepository
                .existsByReview_ReviewIdAndCustomer_UserId(
                        reviewId, customerId);
    }

    @Transactional(readOnly = true)
    public long getHelpfulVoteCount(Long reviewId) {
        return helpfulVoteRepository.countByReview_ReviewId(reviewId);
    }
}