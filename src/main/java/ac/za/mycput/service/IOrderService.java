package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderStatus;

import java.util.List;

public interface IOrderService extends IService<Order, Long> {

    List<Order> findByCustomer(Customer customer);

    List<Order> findByStatus(OrderStatus status);
}