package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CartService {

    Cart create(Cart cart);

    Cart read(Long cartId);

    Cart update(Cart cart);

    boolean delete(Long cartId);

    List<Cart> getAll();

    Cart findByCustomer(Customer customer);

    List<Cart> findByCreatedDate(LocalDate createdDate);

    BigDecimal getCartTotal(Long cartId);
}