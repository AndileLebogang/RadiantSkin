//package ac.za.mycput.factory;
//
//// Lebogang Andile Mahlangu  230561454 //
//import ac.za.mycput.domain.Cart;
//import ac.za.mycput.domain.CartItem;
//import ac.za.mycput.domain.Product;
//import ac.za.mycput.util.Helper;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//
//class CartItemFactoryTest {
//
//    @Test
//    void createCartItem() {
//
//        Cart cart = new Cart.Builder()
//                .setCartId(1L)
//                .build();
//
//        Product product = new Product.Builder()
//                .setProductId(1L)
//                .build();
//
//        CartItem cartItem = CartItemFactory.createCartItem(
//                1L,
//                2,
//                cart,
//                product
//        );
//
//        assertNotNull(cartItem);
//        assertEquals(1L, cartItem.getCartItemId());
//        assertEquals(2, cartItem.getQuantity());
//        assertEquals(cart, cartItem.getCart());
//        assertEquals(product, cartItem.getProduct());
//    }
//
//    @Test
//    void createCartItemWithInvalidQuantity() {
//
//        Cart cart = new Cart.Builder()
//                .setCartId(1L)
//                .build();
//
//        Product product = new Product.Builder()
//                .setProductId(1L)
//                .build();
//
//        CartItem cartItem = CartItemFactory.createCartItem(
//                1L,
//                0,
//                cart,
//                product
//        );
//
//        assertNull(cartItem);
//    }
//}
