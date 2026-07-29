package ac.za.mycput.controller;

/*
* Name: Siphokazi Malingatshoni
* Student Number: 222868708
 */

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.ReviewFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ReviewControllerTest {

    private static Customer customer;
    private static SkinCareProduct product;
    private static Review review;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/review";
    private static final String CUSTOMER_URL = "http://localhost:8080/customer";
    private static final String SKINCARE_URL = "http://localhost:8080/skincare";

    @BeforeAll
    static void setup() {
        customer = CustomerFactory.createCustomer(
                "Siphokazi",
                "Malingatshoni",
                "siphokazi.review.test@example.com",
                "Password123",
                "0821234567"
        );

        product = new SkinCareProduct.Builder()
                .setName("Hydrating Facial Serum")
                .setDescription("Lightweight serum with hyaluronic acid for deep hydration.")
                .setBrand("RadiantSkin")
                .setPrice(new BigDecimal("249.99"))
                .setStockQuantity(50)
                .setImageUrl("https://example.com/images/serum.jpg")
                .setVolumeMl(30)
                .setUsageInstructions("Apply 2-3 drops to clean skin morning and night.")
                .build();
    }

    @Test
    void a_create() {

        ResponseEntity<Customer> customerResponse =
                this.restTemplate.postForEntity(CUSTOMER_URL + "/create", customer, Customer.class);
        assertNotNull(customerResponse.getBody());
        customer = customerResponse.getBody();

        ResponseEntity<SkinCareProduct> productResponse =
                this.restTemplate.postForEntity(SKINCARE_URL + "/create", product, SkinCareProduct.class);
        assertNotNull(productResponse.getBody());
        product = productResponse.getBody();

        review = ReviewFactory.createReview(
                1L,
                5,
                "Loved this serum, noticeable glow within a week!",
                LocalDate.now(),
                customer,
                product
        );

        String url = BASE_URL + "/create";

        ResponseEntity<Review> postResponse =
                this.restTemplate.postForEntity(url, review, Review.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        review = postResponse.getBody();

        System.out.println("Created: " + review);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + review.getReviewId();

        ResponseEntity<Review> response =
                this.restTemplate.getForEntity(url, Review.class);

        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {

        review.setRating(4);
        review.setComment("Still great, updated after a month of use.");

        String url = BASE_URL + "/update";

        this.restTemplate.put(url, review);

        ResponseEntity<Review> response =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + review.getReviewId(), Review.class);

        assertNotNull(response.getBody());

        System.out.println("Updated: " + response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<Review[]> response =
                this.restTemplate.getForEntity(url, Review[].class);

        assertNotNull(response.getBody());

        System.out.println("Get All:");

        for (Review r : response.getBody()) {
            System.out.println(r);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + review.getReviewId();

        this.restTemplate.delete(url);

        System.out.println("Deleted review with id: " + review.getReviewId());
    }
}
