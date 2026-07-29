/* CartItem.java

        Cart Item POJO class

        Author: Lebogang Andile Mahlangu (230561454)

        Date: 20 June 2026 */

package ac.za.mycput.domain;

import jakarta.persistence.*;

@Entity
public class CartItem  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private CartItem() {
    }

    private CartItem(Builder builder) {
        this.cartItemId = builder.cartItemId;
        this.quantity = builder.quantity;
        this.cart = builder.cart;
        this.product = builder.product;
    }



    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", quantity=" + quantity +
                ", cart=" + cart +
                ", product=" + product +
                '}';
    }

    public static class Builder {
        private Long cartItemId;
        private int quantity;
        private Cart cart;
        private Product product;

        public Builder setCartItemId(Long cartItemId) {
            this.cartItemId = cartItemId;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setCart(Cart cart) {
            this.cart = cart;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder copy(CartItem cartItem) {
            this.cartItemId = cartItem.cartItemId;
            this.quantity = cartItem.quantity;
            this.cart = cartItem.cart;
            this.product = cartItem.product;
            return this;
        }

        /*
         * Builds and returns a CartItem object.
         */
        public CartItem build() {
            return new CartItem(this);
        }
    }

}
