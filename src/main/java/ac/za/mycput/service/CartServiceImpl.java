package ac.za.mycput.service;

// Lebogang Andile Mahlangu 230561454 //
import ac.za.mycput.domain.Cart;
import ac.za.mycput.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart read(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    @Override
    public Cart update(Cart cart) {
        if (cartRepository.existsById(cart.getCartId())) {
            return cartRepository.save(cart);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Cart> getAll() {
        return  cartRepository.findAll();
    }
}
