package ac.za.mycput.controller;

import ac.za.mycput.domain.Payment;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.domain.PaymentStatus;
import ac.za.mycput.factory.PaymentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class PaymentControllerTest {

    private static Payment payment;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/payment";

    @BeforeAll
    static void setup() {

        payment = PaymentFactory.createPayment(
                1L,
                599.99,
                PaymentStatus.SUCCESSFUL,
                PaymentMethod.CARD,
                "TXN001"
        );
    }

    @Test
    void a_create() {

        String url = BASE_URL + "/create";

        ResponseEntity<Payment> postResponse =
                this.restTemplate.postForEntity(url, payment, Payment.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        payment = postResponse.getBody();

        System.out.println(payment);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + payment.getPaymentId();

        ResponseEntity<Payment> response =
                this.restTemplate.getForEntity(url, Payment.class);

        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void c_update() {

        Payment updated = new Payment.Builder()
                .copy(payment)
                .setAmount(799.99)
                .build();

        String url = BASE_URL + "/update";

        this.restTemplate.put(url, updated);

        ResponseEntity<Payment> response =
                this.restTemplate.getForEntity(
                        BASE_URL + "/read/" + updated.getPaymentId(),
                        Payment.class);

        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<Payment[]> response =
                this.restTemplate.getForEntity(url, Payment[].class);

        assertNotNull(response.getBody());

        for (Payment p : response.getBody()) {
            System.out.println(p);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + payment.getPaymentId();

        this.restTemplate.delete(url);

        System.out.println("Deleted Payment with ID: " + payment.getPaymentId());
    }
}