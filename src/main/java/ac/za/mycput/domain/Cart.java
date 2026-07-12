/* Cart.java

        Cart POJO class

        Author: Lebogang Andile Mahlangu (230561454)

        Date: 20 June 2026 */

package ac.za.mycput.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;


@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    private LocalDate createdDate;

    //One customer owns one cart
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    private Cart() {

    }

    private Cart(Builder builder) {
        this.cartId = builder.cartId;
        this.createdDate = builder.createdDate;
        this.customer = builder.customer;
        this.cartItems = builder.cartItems;

    }

    public Long getCartId() {
        return cartId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public String toString() {
        return "Cart.Builder{" +
                "cartId=" + cartId +
                ", createdDate=" + createdDate +
                ", customer=" + customer +
                ", cartItems=" + cartItems +
                '}';
    }

    public static class Builder {
        private Long cartId;
        private LocalDate createdDate;
        private Customer customer;
        private List<CartItem> cartItems = new ArrayList<>();

        public Builder setCartId(Long cartId) {
            this.cartId = cartId;
            return this;
        }

        public Builder setCreatedDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }



        public Builder copy(Cart cart) {
            this.cartId = cart.cartId;
            this.createdDate = cart.createdDate;
            this.customer = cart.customer;
            this.cartItems = cart.cartItems;
            return this;
        }

        public Cart build() {
            return new Cart(this);
        }
    }

}
