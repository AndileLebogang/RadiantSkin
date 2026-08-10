package ac.za.mycput.controller;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Role;
import ac.za.mycput.factory.CustomerFactory;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTest {

    private static Customer customer;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/customer";
    }

    @BeforeAll
    static void setup() {

        customer = CustomerFactory.createCustomer(
                null,
                "Tebogo",
                "Makgato",
                "tebogo@gmail.com",
                "Password123",
                "0712345678"
        );

        assertNotNull(customer);

        assertEquals(
                Role.CUSTOMER,
                customer.getRole()
        );
    }

    @Test
    @Order(1)
    void a_create() {

        String url = getBaseUrl() + "/create";

        ResponseEntity<Customer> response =
                restTemplate.postForEntity(
                        url,
                        customer,
                        Customer.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        customer = response.getBody();

        System.out.println("Created: " + customer);
    }

    @Test
    @Order(2)
    void b_read() {

        assertNotNull(customer);
        assertNotNull(customer.getUserId());

        String url =
                getBaseUrl() + "/read/" + customer.getUserId();

        ResponseEntity<Customer> response =
                restTemplate.getForEntity(
                        url,
                        Customer.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    @Order(3)
    void c_update() {

        Customer updated = new Customer.Builder()
                .copy(customer)
                .setPhoneNumber("0723456789")
                .build();

        String url = getBaseUrl() + "/update";

        restTemplate.put(url, updated);

        ResponseEntity<Customer> response =
                restTemplate.getForEntity(
                        getBaseUrl()
                                + "/read/"
                                + updated.getUserId(),
                        Customer.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Order(4)
    void d_getAll() {

        String url = getBaseUrl() + "/getAll";

        ResponseEntity<Customer[]> response =
                restTemplate.getForEntity(
                        url,
                        Customer[].class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        System.out.println("Get All:");

        for (Customer c : response.getBody()) {
            System.out.println(c);
        }
    }

    @Test
    @Order(5)
    void e_delete() {

        assertNotNull(customer);
        assertNotNull(customer.getUserId());

        String url =
                getBaseUrl()
                        + "/delete/"
                        + customer.getUserId();

        restTemplate.delete(url);

        System.out.println(
                "Deleted customer with id: "
                        + customer.getUserId()
        );
    }
}