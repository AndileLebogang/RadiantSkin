package ac.za.mycput.factory;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
/*
*Name: Siphokazi Malingatshoni
* Student Number: 222868708
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.AssertionsKt.assertNull;

public class ReviewFactoryTest {
    @Test
    void testcreateReview(){

        Product product = new Product("P001,Laptop");
        Customer customer = new Customer("C001","Sipho", null);

        Review review = ReviewFactory.createReview(
                1L,
                5,
                "Great product!",
                LocalDate.now,
                product,
                customer
        );
        assertNotNull(review);
        assertEquals(1L, review.getReviewId());
        assertEquals(5,review.getRating());
        assertEquals("Greate product!",review.getComment());
        assertEquals(product,review.getProduct());
        assertEquals(customer,review.Customer());
    }
    @Test
    void testcreateReviewWithNullComment(){

        Customer customer = new Customer("C001","Sipho", null);
        Product product = new Product("P001,Laptop");

        Review review = ReviewFactory.createReview(
                1L,
                5,
                null,
                LocalDate.now,
                product,
                customer
        );
       assertNull(review);
    }
    @Test
    void testcreateReviewWithInvalidRating(){

        Customer customer = new Customer("C001","Sipho", null);
        Product product = new Product("P001,Laptop");

        Review review = ReviewFactory.createReview(
                1L,
                0,
                "Bad rating",
                LocalDate.now,
                product,
                customer
        );
        assertNull(review);

    }
}
