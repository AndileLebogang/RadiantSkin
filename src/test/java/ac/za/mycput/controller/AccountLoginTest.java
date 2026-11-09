package ac.za.mycput.controller;

import ac.za.mycput.domain.*;
import ac.za.mycput.repository.OrderRepository;
import ac.za.mycput.repository.ProductRepository;
import ac.za.mycput.repository.ReviewRepository;
import ac.za.mycput.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.jpa.show-sql=false")
@AutoConfigureMockMvc
@Transactional(isolation = Isolation.READ_COMMITTED)
class AccountLoginTest {
    @Autowired MockMvc mvc;
    @Autowired CustomerService customers;
    @Autowired ProductRepository products;
    @Autowired OrderRepository orders;
    @Autowired ReviewRepository reviews;
    @Autowired ObjectMapper json;
    Customer customer;
    final String password = "SchoolLogin!2026";

    @BeforeEach
    void registerCustomer() {
        Customer input = new Customer();
        input.setFirstName("Sam");
        input.setLastName("Mokoena");
        input.setEmail("login-" + UUID.randomUUID() + "@example.test");
        input.setPhoneNumber("0712345678");
        input.setPassword(password);
        customer = customers.register(input);
    }

    MockHttpSession login(MockHttpSession previous) throws Exception {
        var result = mvc.perform(post("/auth/login").session(previous).contentType("application/json")
                .content(json.writeValueAsString(Map.of("email", "  " + customer.getEmail().toUpperCase() + "  ",
                        "password", password))))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.userId").value(customer.getUserId()))
                .andExpect(jsonPath("$.phoneNumber").value("0712345678"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.cart").doesNotExist())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    @Test
    void accountLoginRestoresFromServerSessionAndLogoutInvalidatesIt() throws Exception {
        MockHttpSession old = new MockHttpSession();
        MockHttpSession session = login(old);
        assertTrue(old.isInvalid());
        assertNotEquals(old.getId(), session.getId());
        assertEquals(1800, session.getMaxInactiveInterval());
        mvc.perform(get("/auth/session").session(session)).andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
        mvc.perform(get("/auth/session")).andExpect(status().isUnauthorized());
        mvc.perform(post("/auth/logout").session(session)).andExpect(status().isNoContent());
        assertTrue(session.isInvalid());
        mvc.perform(get("/auth/session")).andExpect(status().isUnauthorized());
    }

    @Test
    void incorrectAndMissingCredentialsCannotCreateAnAuthenticatedSession() throws Exception {
        var failed = mvc.perform(post("/auth/login").contentType("application/json")
                .content(json.writeValueAsString(Map.of("email", customer.getEmail(), "password", "wrong"))))
                .andExpect(status().isUnauthorized()).andReturn();
        assertNull(failed.getRequest().getSession(false));
        mvc.perform(post("/auth/login").contentType("application/json").content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loggedInCustomerReviewPersistsWithoutCredentialsAndLogoutPreventsAnother() throws Exception {
        MockHttpSession session = login(new MockHttpSession());
        Product product = products.saveAndFlush(new SkinCareProduct.Builder().setName("Session review product")
                .setPrice(new BigDecimal("25.00")).setStockQuantity(2).build());
        var submission = Map.of("productId", product.getProductId(), "rating", 5,
                "comment", "  Gentle on my skin  ", "customerId", customer.getUserId() + 1000);
        var result = mvc.perform(post("/review/submit").session(session).contentType("application/json")
                        .content(json.writeValueAsString(submission)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value("Gentle on my skin"))
                .andExpect(jsonPath("$.customer").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist()).andReturn();
        long id = json.readTree(result.getResponse().getContentAsString()).get("reviewId").asLong();
        Review saved = reviews.findById(id).orElseThrow();
        assertEquals(customer.getUserId(), saved.getCustomer().getUserId());
        assertEquals(product.getProductId(), saved.getProduct().getProductId());
        mvc.perform(get("/review/product/" + product.getProductId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].reviewId").value(id));
        mvc.perform(post("/auth/logout").session(session)).andExpect(status().isNoContent());
        mvc.perform(post("/review/submit").contentType("application/json")
                        .content(json.writeValueAsString(submission)))
                .andExpect(status().isUnauthorized());
        assertEquals(1, reviews.findByProduct(product).size());
    }

    @Test
    void checkoutAssociatesOrderWithTheAuthenticatedCustomer() throws Exception {
        MockHttpSession session = login(new MockHttpSession());
        Product product = products.saveAndFlush(new SkinCareProduct.Builder().setName("Login checkout product")
                .setPrice(new BigDecimal("25.00")).setStockQuantity(2).build());
        var request = Map.of("requestId", UUID.randomUUID(), "expectedTotal", 25,
                "items", List.of(Map.of("productId", product.getProductId(), "quantity", 1)),
                "details", Map.of("firstName", "Sam", "lastName", "Mokoena", "email", customer.getEmail(),
                        "phoneNumber", "0712345678", "street", "1 Main Street", "city", "Pretoria",
                        "province", "Gauteng", "postalCode", "0001", "country", "South Africa"));
        var result = mvc.perform(post("/checkout/place").session(session).contentType("application/json")
                .content(json.writeValueAsString(request))).andExpect(status().isCreated()).andReturn();
        long orderId = json.readTree(result.getResponse().getContentAsString()).get("orderId").asLong();
        assertEquals(customer.getUserId(), orders.findById(orderId).orElseThrow().getCustomer().getUserId());
    }
}
