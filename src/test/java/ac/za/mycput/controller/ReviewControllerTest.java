package ac.za.mycput.controller;

import ac.za.mycput.domain.*;
import ac.za.mycput.repository.*;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.jpa.show-sql=false")
@AutoConfigureMockMvc
@Transactional
class ReviewControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired EntityManager entities;
    @Autowired CustomerService customers;
    @Autowired ProductRepository products;
    @Autowired ReviewRepository reviews;
    @Autowired ReviewHelpfulVoteRepository votes;
    @Autowired UserRepository users;
    @Autowired AccountSession sessions;
    Product product;
    MockHttpSession customerSession;
    MockHttpSession anotherCustomerSession;
    MockHttpSession adminSession;

    MockHttpSession session(User user) {
        var request = new MockHttpServletRequest();
        sessions.start(request, user);
        return (MockHttpSession) request.getSession();
    }

    Customer customer() {
        return customers.register(new Customer.Builder().setFirstName("Review").setLastName("Tester")
                .setEmail("review-" + UUID.randomUUID() + "@example.test").setPhoneNumber("0712345678")
                .setPassword("ReviewTest123!").build());
    }

    @BeforeEach
    void setup() {
        product = products.saveAndFlush(new SkinCareProduct.Builder().setName("Review integration product")
                .setPrice(new BigDecimal("25.00")).setStockQuantity(5).build());
        customerSession = session(customer());
        anotherCustomerSession = session(customer());
        Admin admin = new Admin();
        admin.setEmail("staff-" + UUID.randomUUID() + "@example.test");
        admin.setRole(Role.ADMIN);
        adminSession = session(users.saveAndFlush(admin));
    }

    long submit() throws Exception {
        var response = mvc.perform(post("/review/submit").session(customerSession)
                .contentType("application/json").content(json.writeValueAsString(Map.of(
                        "productId", product.getProductId(), "rating", 5, "comment", "Gentle daily care"))))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.customer").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist()).andReturn().getResponse();
        return json.readTree(response.getContentAsString()).get("reviewId").asLong();
    }

    @Test
    void customerCanCreateReadEditAndDeleteTheirPersistedReview() throws Exception {
        long id = submit();
        entities.flush(); entities.clear();
        assertEquals("Gentle daily care", reviews.findById(id).orElseThrow().getComment());
        mvc.perform(get("/review/product/" + product.getProductId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content[0].reviewId").value(id));
        mvc.perform(put("/review/my-reviews/" + id).session(customerSession)
                        .contentType("application/json").content("{\"rating\":4,\"comment\":\"Updated daily care\"}"))
                .andExpect(status().isOk());
        entities.flush(); entities.clear();
        assertEquals("Updated daily care", reviews.findById(id).orElseThrow().getComment());
        mvc.perform(get("/review/my-reviews").session(customerSession))
                .andExpect(jsonPath("$[0].comment").value("Updated daily care"));
        mvc.perform(delete("/review/my-reviews/" + id).session(customerSession))
                .andExpect(status().isNoContent());
        entities.flush(); entities.clear();
        assertFalse(reviews.existsById(id));
    }

    @Test
    void customerCanDeleteAReviewThatHasHelpfulVotes() throws Exception {
        long id = submit();
        mvc.perform(post("/review/" + id + "/helpful").session(anotherCustomerSession))
                .andExpect(status().isOk());
        mvc.perform(delete("/review/my-reviews/" + id).session(customerSession))
                .andExpect(status().isNoContent());
        entities.flush(); entities.clear();
        assertFalse(reviews.existsById(id));
        assertEquals(0, votes.countByReview_ReviewId(id));
    }

    @Test
    void anotherCustomerCannotEditOrDeleteTheReview() throws Exception {
        long id = submit();
        mvc.perform(put("/review/my-reviews/" + id).session(anotherCustomerSession)
                        .contentType("application/json").content("{\"rating\":1,\"comment\":\"Changed by someone else\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/review/my-reviews/" + id).session(anotherCustomerSession))
                .andExpect(status().isForbidden());
        entities.flush(); entities.clear();
        assertEquals("Gentle daily care", reviews.findById(id).orElseThrow().getComment());
    }

    @Test
    void helpfulVotesAndAdminModerationPersistAndUpdatePublicResults() throws Exception {
        long id = submit();
        mvc.perform(post("/review/" + id + "/helpful").session(anotherCustomerSession))
                .andExpect(status().isOk()).andExpect(jsonPath("$.helpfulCount").value(1));
        mvc.perform(post("/review/" + id + "/helpful").session(anotherCustomerSession))
                .andExpect(status().isConflict());
        entities.flush(); entities.clear();
        assertEquals(1, reviews.findById(id).orElseThrow().getHelpfulCount());
        mvc.perform(put("/review/admin/" + id + "/status").session(adminSession)
                        .contentType("application/json").content("{\"status\":\"HIDDEN\"}"))
                .andExpect(status().isOk());
        entities.flush(); entities.clear();
        assertEquals(ReviewStatus.HIDDEN, reviews.findById(id).orElseThrow().getStatus());
        mvc.perform(get("/review/product/" + product.getProductId()))
                .andExpect(jsonPath("$.totalElements").value(0));
        mvc.perform(put("/review/admin/" + id + "/status").session(adminSession)
                        .contentType("application/json").content("{\"status\":\"APPROVED\"}"))
                .andExpect(status().isOk());
        mvc.perform(get("/review/product/" + product.getProductId()))
                .andExpect(jsonPath("$.content[0].reviewId").value(id));
    }
}
