package ac.za.mycput.service;

import ac.za.mycput.domain.CartItem;

import java.util.List;

public interface CartItemService extends IService<CartItem, Long> {

    List<CartItem> getAll();
}
