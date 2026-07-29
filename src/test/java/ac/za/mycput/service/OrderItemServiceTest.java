/* OrderItemServiceTest.java

     OrderItemService test class

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.HairCareProduct;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.HairCareFactory;
import ac.za.mycput.factory.OrderFactory;
import ac.za.mycput.factory.OrderItemFactory;
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
class OrderItemServiceTest {

    @Autowired
    private OrderItemService service;

    private final Customer customer = CustomerFactory.createCustomer(
            "John",
            "Doe",
            "john.orderitem.service@gmail.com",
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

    private final HairCareProduct product = HairCareFactory.createHairCareProduct(
            "Argan Oil Shampoo",
            "Nourishing shampoo for dry hair",
            "RadiantSkin",
            new BigDecimal("120.00"),
            50,
            "https://example.com/image.jpg",
            250,
            "Dryness"
    );

    private final OrderItem orderItem = OrderItemFactory.createOrderItem(
            2001L,
            2,
            new BigDecimal("120.00"),
            order,
            product
    );

    @Test
    void a_create() {

        OrderItem created = service.create(orderItem);

        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {

        OrderItem read = service.read(orderItem.getOrderItemId());

        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {

        OrderItem updated = new OrderItem.Builder()
                .copy(orderItem)
                .setQuantity(3)
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        assertEquals(3, updated.getQuantity());

        System.out.println("Updated: " + updated);
    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(orderItem.getOrderItemId());

        assertTrue(deleted);
        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {

        List<OrderItem> items = service.getAll();

        assertNotNull(items);

        items.forEach(System.out::println);
    }

    @Test
    void f_findByOrder() {

        List<OrderItem> items = service.findByOrder(order);

        assertNotNull(items);

        items.forEach(System.out::println);
    }

    @Test
    void g_findByProduct() {

        List<OrderItem> items = service.findByProduct(product);

        assertNotNull(items);

        items.forEach(System.out::println);
    }
}
