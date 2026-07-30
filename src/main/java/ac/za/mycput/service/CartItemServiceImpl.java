/* CartItemServiceImpl.java

        CartItemService implementation

        Author: Lebogang Andile Mahlangu (230561454) */

package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository repo;

    @Autowired
    public CartItemServiceImpl(CartItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public CartItem create(CartItem cartItem) {
        return this.repo.save(cartItem);
    }

    @Override
    public CartItem read(Long cartItemId) {
        return this.repo.findById(cartItemId).orElse(null);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        return this.repo.save(cartItem);
    }

    @Override
    public boolean delete(Long cartItemId) {
        this.repo.deleteById(cartItemId);
        return true;
    }

    @Override
    public List<CartItem> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<CartItem> findByCart(Cart cart) {
        return this.repo.findByCart(cart);
    }

    @Override
    public List<CartItem> findByProduct(Product product) {
        return this.repo.findByProduct(product);
    }
}
