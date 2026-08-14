package ac.za.mycput.controller;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.OrderFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class OrderControllerTest {

    private static Customer customer;
    private static Order order;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/order";
    private static final String CUSTOMER_URL = "http://localhost:8080/customer";

    @BeforeAll
    static void setup() {
        customer = CustomerFactory.createCustomer(
                "Lindiwe",
                "Dlamini",
                "lindiwe.order.test@example.com",
                "Password123",
                "0821234567"
        );
    }

    @Test
    void a_create() {

        ResponseEntity<Customer> customerResponse =
                this.restTemplate.postForEntity(CUSTOMER_URL + "/create", customer, Customer.class);
        assertNotNull(customerResponse.getBody());
        customer = customerResponse.getBody();

        order = OrderFactory.createOrder(
                1L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("499.98"),
                customer,
                null,
                null
        );

        String url = BASE_URL + "/create";

        ResponseEntity<Order> postResponse =
                this.restTemplate.postForEntity(url, order, Order.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        order = postResponse.getBody();

        System.out.println("Created: " + order);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + order.getOrderId();

        ResponseEntity<Order> response =
                this.restTemplate.getForEntity(url, Order.class);

        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {

        order.setStatus(OrderStatus.PROCESSING);

        String url = BASE_URL + "/update";

        this.restTemplate.put(url, order);

        ResponseEntity<Order> response =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + order.getOrderId(), Order.class);

        assertNotNull(response.getBody());

        System.out.println("Updated: " + response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<Order[]> response =
                this.restTemplate.getForEntity(url, Order[].class);

        assertNotNull(response.getBody());

        System.out.println("Get All:");

        for (Order o : response.getBody()) {
            System.out.println(o);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + order.getOrderId();

        this.restTemplate.delete(url);

        System.out.println("Deleted order with id: " + order.getOrderId());
    }
}
