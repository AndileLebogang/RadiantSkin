package ac.za.mycput.domain;

// Tebogo Makgato 230116086

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends User {

    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    public Customer() {
    }

    private Customer(Builder builder) {
        super(builder.userId, builder.firstName, builder.lastName, builder.email, builder.password);
        this.phoneNumber = builder.phoneNumber;
        this.addresses = builder.addresses;
        this.cart = builder.cart;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Customer [phoneNumber=" + phoneNumber + "] " + super.toString();
    }

    public static class Builder {
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phoneNumber;
        private List<Address> addresses = new ArrayList<>();
        private Cart cart;

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setAddresses(List<Address> addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder setCart(Cart cart) {
            this.cart = cart;
            return this;
        }
        public Builder copy(Customer customer) {
            this.userId = customer.getUserId();
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.email = customer.getEmail();
            this.password = customer.getPassword();
            this.phoneNumber = customer.getPhoneNumber();
            this.addresses = customer.getAddresses();
            this.cart = customer.getCart();
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}