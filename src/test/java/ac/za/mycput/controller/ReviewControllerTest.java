package ac.za.mycput.controller;

/*
//Name: Siphokazi Malingatshoni
// Student number: 222868708
 */

import ac.za.mycput.domain.Address;
import ac.za.mycput.domain.Review;
import ac.za.mycput.factory.ReviewFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
 class ReviewControllerTest {

  @Autowired
  privateTestRestTemplate restTemplate;

  private Review review;
  private String baseUrl;

  @BeforeEach
  void setUp() {

   Customer customer = new Customer();
   customer.setCustomerId(1L);
   customer.setName("James");

   Product product = new Product();
   product.setProductId(1L);
   product.setProductName("Laptop");

   review = ReviewFactory.createReview(
           1L,
           5,
           "Good Product",
           LocalDate.now(),
           customer,
           product

   );
    baseUrl = "http://localhost:"+ port + "/review";
  }

  @Test
  void create() {
   String url =baseUrl + "/create";

   ResponseEntity<Review>response = restTemplate.postForEntity(url,review, Review.class);

   assertNotNull(response);
   assertEquals(HttpStatus.OK,response.getStatusCode());

  }

  @Test
  void read() {

   ResponseEntity<Review>created = restTemplate.postForEntity(baseUrl+"/create",review, Review.class);
   Review saved = created.getBody();

   String Url = baseUrl +"/read" + saved.getReviewId();

   ResponseEntity<Review>response = restTemplate.getForEntity(url,Review.class);

   assertNotNull(response);
   assertEquals(HttpStatus.OK,response.getStatusCode());
  }

  @Test
   void update() {
   ResponseEntity<Review>created = restTemplate.postForEntity(baseUrl+"/create",review, Review.class);
   Review saved = created.getBody();

   Review updated = new Review.Builder();
            .copy(saved)
           .setComment("Updated comment")
           .build()

   String Url = baseUrl +"/update";

   ResponseEntity<Review>response= restTemplate.postForEntity(url,updated,Review.class );

   assertNotNull(response);
   assertEquals(HttpStatus.OK,response.getStatusCode());
  }

  @Test
  void delete() {
   restTemplate.postForEntity(baseUrl+"/create",review, Review.class);

   String url = baseUrl + "/delete"+review.getReviewId();
   restTemplate.delete(url);

   ResponseEntity<Review>response = restTemplate.postForEntity(baseUrl+"/read "+ review.getReviewId(), Review.class);

   assertNotNull(response);

  }

  @Test
   void getAll() {
   restTemplate.postForEntity(baseUrl+"/create",review, Review.class);

   ResponseEntity<Review[]> response = restTemplate.getForEntity(baseUrl+"/all",Review[].class );

   assertNotNull(response);
   assertTrue(response.getBody().length > 0);
  }
 }