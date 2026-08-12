package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ICartService extends IService<Cart, Long> {

    Cart findByCustomer(Customer customer);

    List<Cart> findByCreatedDate(LocalDate createdDate);

    BigDecimal getCartTotal(Long cartId);
}