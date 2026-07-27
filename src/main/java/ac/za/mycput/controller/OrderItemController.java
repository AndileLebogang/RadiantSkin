/* OrderItemController.java

     OrderItemController class

     Author: La-eeq Lewis (240696255)

     Date: 19 July 2026 */

package ac.za.mycput.controller;

import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderitem")
public class OrderItemController {

    private final OrderItemService service;

    @Autowired
    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public OrderItem create(@RequestBody OrderItem orderItem) {
        return service.create(orderItem);
    }

    @GetMapping("/read/{orderItemId}")
    public OrderItem read(@PathVariable Long orderItemId) {
        return service.read(orderItemId);
    }

    @PutMapping("/update")
    public OrderItem update(@RequestBody OrderItem orderItem) {
        return service.update(orderItem);
    }

    @DeleteMapping("/delete/{orderItemId}")
    public boolean delete(@PathVariable Long orderItemId) {
        return service.delete(orderItemId);
    }

    @GetMapping("/getAll")
    public List<OrderItem> getAll() {
        return service.getAll();
    }
}