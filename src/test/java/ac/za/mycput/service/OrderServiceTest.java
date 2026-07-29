/* OrderServiceTest.java

     OrderService test class

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.OrderFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class OrderServiceTest {

    @Autowired
    private OrderService service;

    private final Customer customer = CustomerFactory.createCustomer(
            "John",
            "Doe",
            "john.order.service@gmail.com",
            "Password123",
            "0712345678"
    );

    private final Order order = OrderFactory.createOrder(
            1001L,
            LocalDateTime.now(),
            OrderStatus.PENDING,
            new BigDecimal("250.00"),
            customer,
            null,
            null
    );

    @Test
    void a_create() {

        Order created = service.create(order);

        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {

        Order read = service.read(order.getOrderId());

        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {

        Order updated = new Order.Builder()
                .copy(order)
                .setStatus(OrderStatus.PROCESSING)
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        assertEquals(OrderStatus.PROCESSING, updated.getStatus());

        System.out.println("Updated: " + updated);
    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(order.getOrderId());

        assertTrue(deleted);
        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {

        List<Order> orders = service.getAll();

        assertNotNull(orders);

        orders.forEach(System.out::println);
    }

    @Test
    void f_findByCustomer() {

        List<Order> orders = service.findByCustomer(customer);

        assertNotNull(orders);

        orders.forEach(System.out::println);
    }

    @Test
    void g_findByStatus() {

        List<Order> orders = service.findByStatus(OrderStatus.PROCESSING);

        assertNotNull(orders);

        orders.forEach(System.out::println);
    }
}
