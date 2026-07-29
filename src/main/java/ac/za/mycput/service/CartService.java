package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;

import java.util.List;

public interface CartService extends IService<Cart, Long> {

    List<Cart> getAll();
}