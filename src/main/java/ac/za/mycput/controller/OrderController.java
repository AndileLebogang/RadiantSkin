/* OrderController.java

     OrderController class

     Author: La-eeq Lewis (240696255)

     Date: 19 July 2026 */

package ac.za.mycput.controller;

import ac.za.mycput.domain.Order;
import ac.za.mycput.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Order create(@RequestBody Order order) {
        return service.create(order);
    }

    @GetMapping("/read/{orderId}")
    public Order read(@PathVariable Long orderId) {
        return service.read(orderId);
    }

    @PutMapping("/update")
    public Order update(@RequestBody Order order) {
        return service.update(order);
    }

    @DeleteMapping("/delete/{orderId}")
    public boolean delete(@PathVariable Long orderId) {
        return service.delete(orderId);
    }

    @GetMapping("/getAll")
    public List<Order> getAll() {
        return service.getAll();
    }
}