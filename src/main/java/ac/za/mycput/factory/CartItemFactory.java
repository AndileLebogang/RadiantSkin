package ac.za.mycput.factory;

import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.util.Helper;

public class CartItemFactory {

    public static CartItem createCartItem(Long cartItemId, int quantity, Object product) {

        if (!Helper.isValidCartItemId(cartItemId) || !Helper.isValidQuantity(quantity) || !Helper.isValidProduct(product)) {
            return null;
        }

        return new CartItem.Builder()
                .setCartItem(cartItemId)
                .setQuantity(quantity)
                .setProduct(product)
                .build();
    }
}