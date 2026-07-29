package ac.za.mycput.factory;

// Lebogang Andile Mahlangu (230561454)

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.util.Helper;

public class CartItemFactory {

    public static CartItem createCartItem(Long cartItemId, int quantity, Cart cart, Product product) {

        if (!Helper.isValidId(cartItemId) || quantity <= 0 || cart == null || product == null) {
            return null;
        }

        return new CartItem.Builder()
                .setCartItemId(cartItemId)
                .setQuantity(quantity)
                .setCart(cart)
                .setProduct(product)
                .build();
    }
}
