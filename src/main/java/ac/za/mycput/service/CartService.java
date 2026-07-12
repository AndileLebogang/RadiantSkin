package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import java.util.List;

public interface CartService {

    Cart create(Cart cart);

    Cart read(Long cartId);

    Cart update(Cart cart);

    boolean delete(Long cartId);

    List<Cart> getAll();
}