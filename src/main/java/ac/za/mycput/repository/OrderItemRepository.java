/* OrderItemRepository.java

     OrderItemRepository interface

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.repository;

import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByProduct(Product product);
}