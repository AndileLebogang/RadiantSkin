package ac.za.mycput.controller;

/*
* Name: Siphokazi Malingatshoni
* Student Number: 222868708
 */

import ac.za.mycput.domain.Address;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.factory.AddressFactory;
import ac.za.mycput.factory.CustomerFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AddressControllerTest {

    private static Customer customer;
    private static Address address;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/address";
    private static final String CUSTOMER_URL = "http://localhost:8080/customer";

    @BeforeAll
    static void setup() {
        customer = CustomerFactory.createCustomer(
                "Siphokazi",
                "Malingatshoni",
                "siphokazi.malingatshoni@example.com",
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

        address = AddressFactory.createAddress(
                "12 Main Road",
                "Cape Town",
                "Western Cape",
                "8000",
                "South Africa",
                customer
        );

        String url = BASE_URL + "/create";

        ResponseEntity<Address> postResponse =
                this.restTemplate.postForEntity(url, address, Address.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        address = postResponse.getBody();

        System.out.println("Created: " + address);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + address.getAddressId();

        ResponseEntity<Address> response =
                this.restTemplate.getForEntity(url, Address.class);

        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {

        Address updated = new Address.Builder()
                .copy(address)
                .setCity("Gqeberha")
                .build();

        String url = BASE_URL + "/update";

        this.restTemplate.put(url, updated);

        ResponseEntity<Address> response =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + updated.getAddressId(), Address.class);

        assertNotNull(response.getBody());

        System.out.println("Updated: " + response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<Address[]> response =
                this.restTemplate.getForEntity(url, Address[].class);

        assertNotNull(response.getBody());

        System.out.println("Get All:");

        for (Address a : response.getBody()) {
            System.out.println(a);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + address.getAddressId();

        this.restTemplate.delete(url);

        System.out.println("Deleted address with id: " + address.getAddressId());
    }
}
