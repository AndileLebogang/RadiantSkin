package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.ProductFactory;
import ac.za.mycput.factory.ReviewFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@org.junit.jupiter.api.TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ReviewServiceTest {
    @Autowired private ac.za.mycput.repository.CustomerRepository fixtureCustomers;
    @Autowired private ac.za.mycput.repository.ProductRepository fixtureProducts;

    @org.junit.jupiter.api.BeforeAll
    void preparePersistentFixtures() {
        fixtureCustomers.saveAndFlush(customer);
        product.setProductId(null);
        fixtureProducts.saveAndFlush(product);
        review.setReviewId(null);
    }


    @Autowired
    private ReviewService service;

    private final Customer customer = CustomerFactory.createCustomer(
            "Tebogo",
            "Makgato",
            "tebogo@gmail.com",
            "Password123",
            "0712345678"
    );

    private final SkinCareProduct product = ProductFactory.createProduct(
            1L,
            "Vitamin C Serum",
            "Brightening facial serum",
            "RadiantSkin",
            new BigDecimal("299.99"),
            20,
            "serum.jpg",
            30,
            "Apply twice daily after cleansing."
    );

    private final Review review = ReviewFactory.createReview(
            1L,
            5,
            "Excellent product!",
            LocalDate.now(),
            customer,
            product
    );

    @Test
    void a_create() {

        Review created = service.create(review);

        assertNotNull(created);
    }

    @Test
    void b_read() {

        Review read = service.read(review.getReviewId());

        assertNotNull(read);
    }

    @Test
    void c_update() {

        Review updated = new Review.Builder()
                .copy(review)
                .setComment("Very good product!")
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        assertEquals("Very good product!", updated.getComment());

    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(review.getReviewId());

        assertTrue(deleted);
    }

    @Test
    void e_getAll() {

        List<Review> reviews = service.getAll();

        assertNotNull(reviews);

    }
}