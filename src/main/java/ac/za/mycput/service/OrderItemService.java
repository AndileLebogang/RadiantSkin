/* OrderItemService.java

     OrderItemService class

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {

    private final OrderItemRepository repo;

    @Autowired
    public OrderItemService(OrderItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        return this.repo.save(orderItem);
    }

    @Override
    public OrderItem read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        return this.repo.save(orderItem);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<OrderItem> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<OrderItem> findByOrder(Order order) {
        return this.repo.findByOrder(order);
    }

    @Override
    public List<OrderItem> findByProduct(Product product) {
        return this.repo.findByProduct(product);
    }
}