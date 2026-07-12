package ac.za.mycput.factory;

// Lebogang Andile Mahlangu (230561454)

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.util.Helper;

public class CartFactory {

    public static Cart createCart(Long cartId, LocalDate createdDate, Customer customer, List<CartItem> cartItems) {

        if (createdDate == null || customer == null || !Helper.isValidCartItems(cartItems)) {
            return null;
        }


        return new Cart.Builder()
                .setCartId(cartId)
                .setCreatedDate(createdDate)
                .setCustomer(customer)
                .setCartItems(cartItems)
                .build();
    }
}