package ac.za.mycput.controller;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.factory.CustomerFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class CustomerControllerTest {

    private static Customer customer;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/customer";

    @BeforeAll
    static void setup() {
        customer = CustomerFactory.createCustomer(
                1L,
                "Tebogo",
                "Makgato",
                "tebogo@gmail.com",
                "Password123",
                "0712345678"
        );
    }

    @Test
    void a_create() {

        String url = BASE_URL + "/create";

        ResponseEntity<Customer> postResponse =
                this.restTemplate.postForEntity(url, customer, Customer.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        customer = postResponse.getBody();

        System.out.println("Created: " + customer);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + customer.getUserId();

        ResponseEntity<Customer> response =
                this.restTemplate.getForEntity(url, Customer.class);

        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {

        Customer updated = new Customer.Builder()
                .copy(customer)
                .setPhoneNumber("0723456789")
                .build();

        String url = BASE_URL + "/update";

        this.restTemplate.put(url, updated);

        ResponseEntity<Customer> response =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + updated.getUserId(), Customer.class);

        assertNotNull(response.getBody());

        System.out.println("Updated: " + response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<Customer[]> response =
                this.restTemplate.getForEntity(url, Customer[].class);

        assertNotNull(response.getBody());

        System.out.println("Get All:");

        for (Customer c : response.getBody()) {
            System.out.println(c);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + customer.getUserId();

        this.restTemplate.delete(url);

        System.out.println("Deleted customer with id: " + customer.getUserId());
    }
}