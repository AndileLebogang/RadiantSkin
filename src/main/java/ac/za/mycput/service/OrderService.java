/* OrderService.java

     OrderService class

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository repo;

    @Autowired
    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public Order create(Order order) {
        return this.repo.save(order);
    }

    @Override
    public Order read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Order update(Order order) {
        return this.repo.save(order);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<Order> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return this.repo.findByCustomer(customer);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return this.repo.findByStatus(status);
    }
}