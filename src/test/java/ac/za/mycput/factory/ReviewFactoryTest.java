package ac.za.mycput.factory;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.SkinCareProduct;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
*Name: Siphokazi Malingatshoni
* Student Number: 222868708
 */
public class ReviewFactoryTest {

    private Customer createTestCustomer() {
        return CustomerFactory.createCustomer(
                "Sipho",
                "Malingatshoni",
                "sipho@gmail.com",
                "Password123",
                "0821234567"
        );
    }

    private SkinCareProduct createTestProduct() {
        return SkinCareFactory.createSkinCareProduct(
                "Laptop Cream",
                "A sample product for testing",
                "RadiantSkin",
                new BigDecimal("49.99"),
                10,
                "image.jpg",
                50,
                "Apply as needed"
        );
    }

    @Test
    void testCreateReview() {

        Review review = ReviewFactory.createReview(
                1L,
                5,
                "Great product!",
                LocalDate.now(),
                createTestCustomer(),
                createTestProduct()
        );

        assertNotNull(review);
        assertEquals(1L, review.getReviewId());
        assertEquals(5, review.getRating());
        assertEquals("Great product!", review.getComment());
        assertNotNull(review.getProduct());
        assertNotNull(review.getCustomer());
    }

    @Test
    void testCreateReviewWithNullComment() {

        Review review = ReviewFactory.createReview(
                1L,
                5,
                null,
                LocalDate.now(),
                createTestCustomer(),
                createTestProduct()
        );

        assertNull(review);
    }

    @Test
    void testCreateReviewWithInvalidRating() {

        Review review = ReviewFactory.createReview(
                1L,
                0,
                "Bad rating",
                LocalDate.now(),
                createTestCustomer(),
                createTestProduct()
        );

        assertNull(review);
    }
}
