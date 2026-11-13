package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Product;
import ac.za.mycput.domain.Review;
import ac.za.mycput.repository.CustomerRepository;
import ac.za.mycput.repository.ProductRepository;
import ac.za.mycput.repository.ReviewRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerReviewValidationTest {

    @Test
    void savesAValidTrimmedReviewForExistingCustomerAndProduct() {
        ReviewRepository reviews = mock(ReviewRepository.class);
        CustomerRepository customers = mock(CustomerRepository.class);
        ProductRepository products = mock(ProductRepository.class);
        when(customers.findById(3L)).thenReturn(Optional.of(mock(Customer.class)));
        when(products.findById(8L)).thenReturn(Optional.of(mock(Product.class)));
        when(reviews.save(any(Review.class))).thenAnswer(call -> call.getArgument(0));

        Review saved = new ReviewService(reviews, customers, products, mock(ac.za.mycput.repository.ReviewHelpfulVoteRepository.class))
                .addReview(3L, 8L, 4, "  Nice on my skin  ");

        assertEquals("Nice on my skin", saved.getComment());
        assertEquals(4, saved.getRating());
        assertNotNull(saved.getReviewDate());
    }

    @Test
    void refusesEmptyOrOversizedReviewText() {
        ReviewRepository reviews = mock(ReviewRepository.class);
        CustomerRepository customers = mock(CustomerRepository.class);
        ProductRepository products = mock(ProductRepository.class);
        when(customers.findById(3L)).thenReturn(Optional.of(mock(Customer.class)));
        when(products.findById(8L)).thenReturn(Optional.of(mock(Product.class)));
        ReviewService service = new ReviewService(reviews, customers, products, mock(ac.za.mycput.repository.ReviewHelpfulVoteRepository.class));

        assertThrows(IllegalArgumentException.class, () -> service.addReview(3L, 8L, 5, " "));
        assertThrows(IllegalArgumentException.class, () -> service.addReview(3L, 8L, 5, "x".repeat(241)));
        verifyNoInteractions(reviews);
    }
}
