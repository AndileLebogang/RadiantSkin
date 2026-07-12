package ac.za.mycput.service;

import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.factory.CartItemFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemServiceTest {

    @Test
    void create() {

        Product product = new Product.Builder()
                .setProductId(1L)
                .build();

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                product
        );
        assertNotNull(cartItem);
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