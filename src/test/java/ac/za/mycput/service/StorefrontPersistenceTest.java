package ac.za.mycput.service;

import ac.za.mycput.domain.*;
import ac.za.mycput.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.jpa.show-sql=false")
@AutoConfigureMockMvc
@Transactional
class StorefrontPersistenceTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired EntityManager entities;
    @Autowired ProductRepository products;
    @Autowired CartRepository carts;
    @Autowired OrderRepository orders;
    @Autowired PaymentRepository payments;
    @Autowired AddressRepository addresses;
    @Autowired CustomerService customers;
    Product product;
    MockHttpSession session;

    @BeforeEach void setup() throws Exception {
        product = products.saveAndFlush(new SkinCareProduct.Builder().setName("DEMO persistence product")
                .setPrice(new BigDecimal("24.95")).setStockQuantity(4).build());
        session = (MockHttpSession) mvc.perform(get("/storefront/cart")).andExpect(status().isOk())
                .andReturn().getRequest().getSession();
    }

    String bag(int quantity) throws Exception {
        return json.writeValueAsString(List.of(new StorefrontCartService.Line(product.getProductId(), quantity)));
    }

    @Test void guestBagSurvivesReloadAndRemovalsDeleteRowsAndOtherSessionsCannotReadIt() throws Exception {
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content(bag(2)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].quantity").value(2));
        entities.flush(); entities.clear();
        Long cartId = (Long) session.getAttribute(StorefrontCartService.SESSION_CART);
        assertEquals(2, carts.findById(cartId).orElseThrow().getCartItems().getFirst().getQuantity());
        mvc.perform(get("/storefront/cart").session(session)).andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].customer").doesNotExist());
        mvc.perform(get("/storefront/cart")).andExpect(content().json("[]"));
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content("[]"))
                .andExpect(status().isOk());
        entities.flush(); entities.clear();
        assertTrue(carts.findById(cartId).orElseThrow().getCartItems().isEmpty());
        assertEquals(0L, entities.createQuery("select count(i) from CartItem i where i.cart.cartId = :id", Long.class)
                .setParameter("id", cartId).getSingleResult());
    }

    @Test void invalidBagUpdateKeepsPreviouslySavedContents() throws Exception {
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content(bag(2)))
                .andExpect(status().isOk());
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content(bag(5)))
                .andExpect(status().isConflict());
        mvc.perform(get("/storefront/cart").session(session)).andExpect(jsonPath("$[0].quantity").value(2));
    }

    StorefrontCheckoutService.Request checkout(String total) {
        CheckoutDetails d = new CheckoutDetails();
        d.firstName = "DEMO"; d.lastName = "Customer"; d.email = "demo@example.com";
        d.phoneNumber = "0712345678"; d.street = "1 Demo Street"; d.city = "Pretoria";
        d.province = "Gauteng"; d.postalCode = "0001"; d.country = "South Africa";
        return new StorefrontCheckoutService.Request(UUID.randomUUID(),
                List.of(new StorefrontCheckoutService.Line(product.getProductId(), 2)), d, new BigDecimal(total));
    }

    @Test void checkoutPersistsPendingPaymentAndAddressAndClearsBagExactlyOnce() throws Exception {
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content(bag(2)))
                .andExpect(status().isOk());
        long beforePayments = payments.count();
        long beforeAddresses = addresses.count();
        var request = checkout("49.90");
        var response = mvc.perform(post("/checkout/place").session(session).contentType("application/json")
                .content(json.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andReturn().getResponse().getContentAsString();
        long orderId = json.readTree(response).get("orderId").asLong();
        entities.flush(); entities.clear();
        Order order = orders.findById(orderId).orElseThrow();
        assertEquals("1 Demo Street", order.getDeliveryAddress().getStreet());
        assertEquals(1, order.getPayments().size());
        Payment payment = order.getPayments().getFirst();
        assertNotNull(payment.getPaymentId());
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
        assertEquals(49.90, payment.getAmount(), 0.001);
        assertEquals(orderId, payment.getOrder().getOrderId());
        mvc.perform(get("/storefront/cart").session(session)).andExpect(content().json("[]"));
        // Retrying the old order must not delete a newly saved bag.
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content(bag(1)))
                .andExpect(status().isOk());
        mvc.perform(post("/checkout/place").session(session).contentType("application/json")
                .content(json.writeValueAsString(request))).andExpect(status().isCreated());
        entities.flush(); entities.clear();
        assertEquals(beforePayments + 1, payments.count());
        assertEquals(beforeAddresses + 1, addresses.count());
        assertEquals(2, products.findById(product.getProductId()).orElseThrow().getStockQuantity());
        mvc.perform(get("/storefront/cart").session(session)).andExpect(jsonPath("$[0].quantity").value(1));
    }

    @Test void loginAdoptsGuestBagAndAnotherLoginRestoresItWithoutLeakingOnLogout() throws Exception {
        String email = "demo-" + UUID.randomUUID() + "@example.com";
        Customer customer = customers.register(new Customer.Builder().setFirstName("DEMO").setLastName("Customer")
                .setEmail(email).setPhoneNumber("0712345678").setPassword("DemoPassword123!").build());
        mvc.perform(put("/storefront/cart").session(session).contentType("application/json").content(bag(2)))
                .andExpect(status().isOk());
        String login = json.writeValueAsString(java.util.Map.of("email", email, "password", "DemoPassword123!"));
        var signedIn = (MockHttpSession) mvc.perform(post("/auth/login").session(session)
                .contentType("application/json").content(login)).andExpect(status().isOk())
                .andReturn().getRequest().getSession();
        mvc.perform(get("/storefront/cart").session(signedIn)).andExpect(jsonPath("$[0].quantity").value(2));
        entities.flush(); entities.clear();
        assertEquals(customer.getUserId(), carts.findById((Long) signedIn.getAttribute(StorefrontCartService.SESSION_CART))
                .orElseThrow().getCustomer().getUserId());
        mvc.perform(post("/auth/logout").session(signedIn)).andExpect(status().isNoContent());
        mvc.perform(get("/storefront/cart")).andExpect(content().json("[]"));
        var restored = (MockHttpSession) mvc.perform(post("/auth/login").contentType("application/json").content(login))
                .andExpect(status().isOk()).andReturn().getRequest().getSession();
        mvc.perform(get("/storefront/cart").session(restored)).andExpect(jsonPath("$[0].quantity").value(2));
    }
}
