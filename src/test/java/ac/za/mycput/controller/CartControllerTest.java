package ac.za.mycput.controller;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.factory.CartFactory;
import ac.za.mycput.factory.CustomerFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class CartControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/cart";

    Customer customer = CustomerFactory.createCustomer(
            "John",
            "Doe",
            "john@gmail.com",
            "Password123",
            "0712345678"
    );

    private final Cart cart = CartFactory.createCart(
            1L,
            LocalDate.now(),
            customer,
            new ArrayList<>()
    );

    @Test
    void a_create() {
        customer = restTemplate.postForEntity("/customer/create", java.util.Map.of("firstName", customer.getFirstName(), "lastName", customer.getLastName(), "email", "fixture-" + java.util.UUID.randomUUID() + "@example.test", "password", "Password123", "phoneNumber", customer.getPhoneNumber()), Customer.class).getBody();
        cart.setCustomer(customer);
        cart.setCartId(null);

        ResponseEntity<Cart> response =
                restTemplate.postForEntity(
                        BASE_URL + "/create",
                        cart,
                        Cart.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        cart.setCartId(response.getBody().getCartId());
    }

    @Test
    void b_read() {

        ResponseEntity<Cart> response =
                restTemplate.getForEntity(
                        BASE_URL + "/read/" + cart.getCartId(),
                        Cart.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void c_update() {

        Cart updated = new Cart.Builder()
                .copy(cart)
                .setCreatedDate(LocalDate.now().plusDays(2))
                .build();

        restTemplate.put(
                BASE_URL + "/update",
                updated);

        ResponseEntity<Cart> response =
                restTemplate.getForEntity(
                        BASE_URL + "/read/" + updated.getCartId(),
                        Cart.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void d_delete() {

        restTemplate.delete(
                BASE_URL + "/delete/" + cart.getCartId());

        System.out.println("Delete successful");
    }

    @Test
    void e_getAll() {

        ResponseEntity<List<Cart>> response =
                restTemplate.exchange(
                        BASE_URL + "/getAll",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Cart>>() {
                        });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        if (response.getBody() != null) {
            response.getBody().forEach(System.out::println);
        }
    }
}