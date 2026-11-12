package ac.za.mycput.service;

import ac.za.mycput.domain.*;
import ac.za.mycput.repository.OrderRepository;
import ac.za.mycput.repository.ProductRepository;
import ac.za.mycput.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Runs against the configured local database; all test rows and stock changes roll back. */
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@AutoConfigureMockMvc
@Transactional(isolation = Isolation.READ_COMMITTED)
class StorefrontCheckoutTest {
    @Autowired StorefrontCheckoutService checkout;
    @Autowired ProductRepository products;
    @Autowired OrderRepository orders;
    @Autowired PaymentRepository payments;
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    Product product;
    CheckoutDetails details;

    @BeforeEach
    void setup() {
        product = products.saveAndFlush(new SkinCareProduct.Builder().setName("Checkout integration product")
                .setPrice(new BigDecimal("24.95")).setStockQuantity(3).build());
        details = new CheckoutDetails();
        details.firstName = " Sam ";
        details.lastName = "Mokoena";
        details.email = "SAM@example.com";
        details.phoneNumber = "0712345678";
        details.street = "1 Main Street";
        details.city = "Pretoria";
        details.province = "Gauteng";
        details.postalCode = "0001";
        details.country = "South Africa";
    }

    StorefrontCheckoutService.Request request(int quantity, String total) {
        return new StorefrontCheckoutService.Request(UUID.randomUUID(),
                List.of(new StorefrontCheckoutService.Line(product.getProductId(), quantity)),
                details, new BigDecimal(total));
    }

    @Test
    void savesReceiptItemsAndDeliveryOnceAndReturnsASafeTrackableResponse() throws Exception {
        long before = orders.count();
        var request = request(2, "49.90");
        var response = mvc.perform(post("/checkout/place").contentType("application/json")
                .content(json.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(49.90))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(2))
                .andExpect(jsonPath("$.customer").doesNotExist())
                .andExpect(jsonPath("$.checkoutDetails").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(response).get("orderId").asLong();
        var saved = orders.findById(id).orElseThrow();
        assertEquals("Sam", saved.getCheckoutDetails().firstName);
        assertEquals("sam@example.com", saved.getCheckoutDetails().email);
        assertEquals("1 Main Street", saved.getCheckoutDetails().street);
        assertEquals(1, products.findById(product.getProductId()).orElseThrow().getStockQuantity());
        assertEquals(before + 1, orders.count());

        var repeated = checkout.place(request);
        assertEquals(id, repeated.orderId());
        assertEquals(before + 1, orders.count());
        assertEquals(1, product.getStockQuantity());
        mvc.perform(get("/order/read/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(id))
                .andExpect(jsonPath("$.orderItems[0].product.name").value(product.getName()))
                .andExpect(jsonPath("$.checkoutDetails").doesNotExist());
    }

    @Test
    void stockAndPriceConflictsDoNotSaveOrdersOrChangeStock() {
        long before = orders.count();
        var stockError = assertThrows(ResponseStatusException.class, () -> checkout.place(request(4, "99.80")));
        assertEquals(409, stockError.getStatusCode().value());
        var priceError = assertThrows(ResponseStatusException.class, () -> checkout.place(request(1, "0.01")));
        assertEquals(409, priceError.getStatusCode().value());
        assertEquals(before, orders.count());
        assertEquals(3, product.getStockQuantity());
    }

    @ParameterizedTest
    @EnumSource(PaymentMethod.class)
    void savesSelectedPaymentMethodAsPendingAndDoesNotDuplicateOnRetry(PaymentMethod method) throws Exception {
        var request = new StorefrontCheckoutService.Request(UUID.randomUUID(),
                List.of(new StorefrontCheckoutService.Line(product.getProductId(), 1)),
                details, new BigDecimal("24.95"), method);
        var response = mvc.perform(post("/checkout/place").contentType("application/json")
                        .content(json.writeValueAsString(request)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = json.readTree(response).get("orderId").asLong();
        var saved = payments.findAll().stream()
                .filter(payment -> payment.getOrder().getOrderId().equals(id)).toList();
        assertEquals(1, saved.size());
        assertEquals(method, saved.getFirst().getPaymentMethod());
        assertEquals(PaymentStatus.PENDING, saved.getFirst().getPaymentStatus());
        assertEquals(24.95, saved.getFirst().getAmount(), 0.001);

        assertEquals(id, checkout.place(request).orderId());
        assertEquals(2, product.getStockQuantity());
        assertEquals(1, payments.findAll().stream()
                .filter(payment -> payment.getOrder().getOrderId().equals(id)).count());
    }

    @Test
    void rejectsUnknownPaymentMethodBeforeCreatingOrder() throws Exception {
        long before = orders.count();
        var payload = json.valueToTree(request(1, "24.95"));
        ((com.fasterxml.jackson.databind.node.ObjectNode) payload).put("paymentMethod", "UNKNOWN");
        mvc.perform(post("/checkout/place").contentType("application/json")
                .content(json.writeValueAsString(payload))).andExpect(status().isBadRequest());
        assertEquals(before, orders.count());
        assertEquals(3, product.getStockQuantity());
    }

    @Test
    void supportsOlderCheckoutRequestsWithoutAPaymentMethod() throws Exception {
        var payload = json.valueToTree(request(1, "24.95"));
        ((com.fasterxml.jackson.databind.node.ObjectNode) payload).remove("paymentMethod");
        var response = mvc.perform(post("/checkout/place").contentType("application/json")
                        .content(json.writeValueAsString(payload)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = json.readTree(response).get("orderId").asLong();
        var saved = payments.findAll().stream()
                .filter(payment -> payment.getOrder().getOrderId().equals(id)).findFirst().orElseThrow();
        assertEquals(PaymentMethod.CARD, saved.getPaymentMethod());
        assertEquals(PaymentStatus.PENDING, saved.getPaymentStatus());
    }

    @Test
    void emptyDuplicateNegativeAndInvalidDeliveryRequestsAreRejected() throws Exception {
        long before = orders.count();
        var empty = new StorefrontCheckoutService.Request(UUID.randomUUID(), List.of(), details, BigDecimal.ZERO);
        mvc.perform(post("/checkout/place").contentType("application/json")
                .content(json.writeValueAsString(empty))).andExpect(status().isBadRequest());
        assertThrows(ResponseStatusException.class, () -> checkout.place(request(-1, "24.95")));
        var duplicate = new StorefrontCheckoutService.Request(UUID.randomUUID(),
                List.of(new StorefrontCheckoutService.Line(product.getProductId(), 1),
                        new StorefrontCheckoutService.Line(product.getProductId(), 1)), details, new BigDecimal("49.90"));
        assertThrows(ResponseStatusException.class, () -> checkout.place(duplicate));
        details.postalCode = "invalid";
        assertThrows(ResponseStatusException.class, () -> checkout.place(request(1, "24.95")));
        assertEquals(before, orders.count());
        assertEquals(3, product.getStockQuantity());
    }
}
