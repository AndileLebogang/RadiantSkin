package ac.za.mycput.service;

import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.Product;

import java.util.List;

public interface IOrderItemService extends IService<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByProduct(Product product);
}