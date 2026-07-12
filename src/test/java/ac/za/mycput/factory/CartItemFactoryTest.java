package ac.za.mycput.factory;

import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.util.Helper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CartItemFactoryTest {

    @Test
    void createCartItem() {

        Product product = new Product.Builder()
                .setProductId(1L)
                .build();

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                produt
        );

        assertNotNull(cartItem);
        assertEquals(1L, cartItem.getCartItemId());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(product, cartItem.getProduct());
    }

    @Test
    void createCartItemWithInvalidQuantity() {

        Product product = new Product.Builder()
                .setProductId(1L)
                .build();

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                0,
                product
        );

        assertNull(cartItem);
    }
}