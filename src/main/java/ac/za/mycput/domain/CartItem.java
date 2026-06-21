/* CartItem.java

        Cart Item POJO class

        Author: Lebogang Andile Mahlangu (230561454)

        Date: 20 June 2026 */

package ac.za.mycput.domain;

@Entity
public class CartItem  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItem;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Product product;

    private CartItem() {
    }

    private CartItem(Builder builder) {
        this.cartItem = builder.cartItem;
        this.quantity = builder.quantity;
        this.product = builder.product;
    }

    public Long getCartItem() {
        return cartItem;
    }


    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public static class Builder {
        private Long cartItem;
        private int quantity;
        private Product product;


        /*
         * Builds and returns a CartItem object.
         */
        public CartItem build() {
            return new CartItem(this);
        }
    }

}
