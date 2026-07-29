/* OrderRepository.java

     OrderRepository interface

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.repository;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);

    List<Order> findByStatus(OrderStatus status);
}