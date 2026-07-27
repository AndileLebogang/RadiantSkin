package ac.za.mycput.controller;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    private final CartItemService service;

    @Autowired
    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public CartItem create(@RequestBody CartItem cartItem) {
        return service.create(cartItem);
    }

    @GetMapping("/read/{id}")
    public CartItem read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public CartItem update(@RequestBody CartItem cartItem) {
        return service.update(cartItem);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/all")
    public List<CartItem> getAll() {
        return service.getAll();
    }
}