package ac.za.mycput.controller;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.OrderFactory;
import ac.za.mycput.factory.OrderItemFactory;
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
class OrderItemControllerTest {

    private static Customer customer;
    private static Order order;
    private static SkinCareProduct product;
    private static OrderItem orderItem;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/orderitem";
    private static final String CUSTOMER_URL = "http://localhost:8080/customer";
    private static final String ORDER_URL = "http://localhost:8080/order";
    private static final String SKINCARE_URL = "http://localhost:8080/skincare";

    @BeforeAll
    static void setup() {
        customer = CustomerFactory.createCustomer(
                "Lindiwe",
                "Dlamini",
                "lindiwe.orderitem.test@example.com",
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

        Order newOrder = OrderFactory.createOrder(
                1L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("499.98"),
                customer,
                null,
                null
        );

        ResponseEntity<Order> orderResponse =
                this.restTemplate.postForEntity(ORDER_URL + "/create", newOrder, Order.class);
        assertNotNull(orderResponse.getBody());
        order = orderResponse.getBody();

        orderItem = OrderItemFactory.createOrderItem(
                1L,
                2,
                new BigDecimal("249.99"),
                order,
                product
        );

        String url = BASE_URL + "/create";

        ResponseEntity<OrderItem> postResponse =
                this.restTemplate.postForEntity(url, orderItem, OrderItem.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        orderItem = postResponse.getBody();

        System.out.println("Created: " + orderItem);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + orderItem.getOrderItemId();

        ResponseEntity<OrderItem> response =
                this.restTemplate.getForEntity(url, OrderItem.class);

        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {

        orderItem.setQuantity(3);

        String url = BASE_URL + "/update";

        this.restTemplate.put(url, orderItem);

        ResponseEntity<OrderItem> response =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + orderItem.getOrderItemId(), OrderItem.class);

        assertNotNull(response.getBody());

        System.out.println("Updated: " + response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<OrderItem[]> response =
                this.restTemplate.getForEntity(url, OrderItem[].class);

        assertNotNull(response.getBody());

        System.out.println("Get All:");

        for (OrderItem oi : response.getBody()) {
            System.out.println(oi);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + orderItem.getOrderItemId();

        this.restTemplate.delete(url);

        System.out.println("Deleted order item with id: " + orderItem.getOrderItemId());
    }
}
