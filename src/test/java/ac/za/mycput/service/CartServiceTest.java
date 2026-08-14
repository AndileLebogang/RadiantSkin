package ac.za.mycput.service;

// Lebogang Andile Mahlangu  230561454 //

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.factory.CartFactory;
import ac.za.mycput.factory.CustomerFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class CartServiceTest {

    @Autowired
    private CartService service;

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

        Cart created = service.create(cart);

        assertNotNull(created);

        System.out.println(created);
    }

    @Test
    void b_read() {

        Cart read = service.read(cart.getCartId());

        assertNotNull(read);

        System.out.println(read);
    }

    @Test
    void c_update() {

        Cart updated = new Cart.Builder()
                .copy(cart)
                .setCreatedDate(LocalDate.now().plusDays(1))
                .build();

        updated = service.update(updated);

        assertNotNull(updated);

        System.out.println(updated);
    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(cart.getCartId());

        assertTrue(deleted);

        System.out.println("Deleted");
    }

    @Test
    void e_getAll() {

        assertNotNull(service.getAll());

        service.getAll().forEach(System.out::println);
    }
}