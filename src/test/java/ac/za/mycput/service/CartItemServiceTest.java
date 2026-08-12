package ac.za.mycput.service;

// Lebogang Andile Mahlangu  230561454 //
import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.factory.CartItemFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemServiceTest {

    @Test
    void create() {

        Cart cart = new Cart.Builder()
                .setCartId(1L)
                .build();

        Product product = new SkinCareProduct.Builder()
                .setProductId(1L)
                .build();

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                cart,
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