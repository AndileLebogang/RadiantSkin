package ac.za.mycput.factory;

// Lebogang Andile Mahlangu  230561454 //


import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartFactoryTest {

    Customer customer = CustomerFactory.createCustomer(
            "John",
            "Doe",
            "john@gmail.com",
            "Password123",
            "0712345678"
    );

    @Test
    void createCart() {

        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                customer,
                new ArrayList<>()
        );

        assertNotNull(cart);
    }

    @Test
    void createCartWithNullCustomer() {

        Cart cart = CartFactory.createCart(
                1L,
                LocalDate.now(),
                null,
                new ArrayList<>()
        );

        assertNull(cart);
    }

    @Test
    void createCartWithNullDate() {

        Cart cart = CartFactory.createCart(
                1L,
                null,
                customer,
                new ArrayList<>()
        );

        assertNull(cart);
    }
}