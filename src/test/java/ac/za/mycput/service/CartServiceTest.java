package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.factory.CartFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    @Test
    void create() {

        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        assertNotNull(cart);
    }

    @Test
    void read() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }
}