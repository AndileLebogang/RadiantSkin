/* CartServiceImpl.java

        CartService implementation

        Author: Lebogang Andile Mahlangu (230561454) */

package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository repo;

    @Autowired
    public CartServiceImpl(CartRepository repo) {
        this.repo = repo;
    }

    @Override
    public Cart create(Cart cart) {
        return this.repo.save(cart);
    }

    @Override
    public Cart read(Long cartId) {
        return this.repo.findById(cartId).orElse(null);
    }

    @Override
    public Cart update(Cart cart) {
        return this.repo.save(cart);
    }

    @Override
    public boolean delete(Long cartId) {
        this.repo.deleteById(cartId);
        return true;
    }

    @Override
    public List<Cart> getAll() {
        return this.repo.findAll();
    }

    @Override
    public Cart findByCustomer(Customer customer) {
        return this.repo.findByCustomer(customer);
    }

    @Override
    public List<Cart> findByCreatedDate(LocalDate createdDate) {
        return this.repo.findByCreatedDate(createdDate);
    }

    @Override
    public BigDecimal getCartTotal(Long cartId) {
        Cart cart = this.read(cartId);
        if (cart == null || cart.getCartItems() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct() != null && item.getProduct().getPrice() != null) {
                total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return total;
    }
}
