package ac.za.mycput.service;

// Lebogang Andile Mahlangu 230561454

import ac.za.mycput.domain.CartItem;
import ac.za.mycput.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem create(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem read(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        if (cartItemRepository.existsById(cartItem.getCartItemId())) {
            return cartItemRepository.save(cartItem);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (cartItemRepository.existsById(id)) {
            cartItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<CartItem> getAll() {
        return cartItemRepository.findAll();
    }
}