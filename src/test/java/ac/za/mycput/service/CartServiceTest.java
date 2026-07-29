package ac.za.mycput.service;

// Lebogang Andile Mahlangu  230561454 //
import ac.za.mycput.domain.Cart;
import ac.za.mycput.factory.CartFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    private CartService service;

    @Test
    void create() {
        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        Cart created = service.create(cart);
        assertNotNull(created);
    }

    @Test
    void read() {
        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        Cart created = service.create(cart);
        Cart found = service.read(created.getCartId());

        assertNotNull(found);
        assertEquals(created.getCartId(), found.getCartId());
    }

    @Test
    void update() {
        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        Cart created = service.create(cart);
        Cart updatedCart = new Cart.Builder()
                .copy(created)
                .setCreatedDate(LocalDate.now())
                .build();

        Cart updated = service.update(updatedCart);
        assertNotNull(updated);
        assertEquals(created.getCartId(), updated.getCartId());
    }

    @Test
    void delete() {
        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        Cart created = service.create(cart);
        boolean deleted = service.delete(created.getCartId());

        assertTrue(deleted);
        assertNull(service.read(created.getCartId()));
    }

    @Test
    void getAll() {
        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        service.create(cart);
        List<Cart> carts = service.getAll();
        assertNotNull(carts);
        assertFalse(carts.isEmpty());
    }
}