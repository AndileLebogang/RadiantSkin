package ac.za.mycput.factory;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.util.Helper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartFactoryTest {

    @Test
    void createCart() {

        Customer customer = new Customer.Builder()
                .setCustomerId(1L)
                .build();

        List<CartItem> cartItem new ArrayList<>();

        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                customer,
                cartItems
        );

        assertNotNull(cart);
        assertEquals(1L, cart.getCartId());
        assertEquals(customer, cart.getCustomer());
        assertEquals(cartItems, cart.getCartItems());
    }

    @Test
    void createCartWithNullCustomer() {
        List<CartItem> cartItems = new ArrayList<>();

        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                cartItems
        );

        assertNull(cart);
    }
}