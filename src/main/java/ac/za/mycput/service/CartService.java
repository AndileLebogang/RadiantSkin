/* CartService.java

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
public class CartService implements ICartService {

    private final CartRepository repo;

    @Autowired
    public CartService(CartRepository repo) {
        this.repo = repo;
    }

    @Override
    public Cart create(Cart cart) {
        return repo.save(cart);
    }

    @Override
    public Cart read(Long cartId) {
        return repo.findById(cartId).orElse(null);
    }

    @Override
    public Cart update(Cart cart) {
        return repo.save(cart);
    }

    @Override
    public boolean delete(Long cartId) {
        repo.deleteById(cartId);
        return true;
    }

    @Override
    public List<Cart> getAll() {
        return repo.findAll();
    }

    @Override
    public Cart findByCustomer(Customer customer) {
        return repo.findByCustomer(customer);
    }

    @Override
    public List<Cart> findByCreatedDate(LocalDate createdDate) {
        return repo.findByCreatedDate(createdDate);
    }

    @Override
    public BigDecimal getCartTotal(Long cartId) {

        Cart cart = read(cartId);

        if (cart == null || cart.getCartItems() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {

            if (item.getProduct() != null &&
                    item.getProduct().getPrice() != null) {

                total = total.add(
                        item.getProduct()
                                .getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                );
            }
        }

        return total;
    }
}