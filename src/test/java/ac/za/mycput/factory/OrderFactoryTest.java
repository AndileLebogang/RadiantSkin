package ac.za.mycput.factory;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryTest {

    private Customer createTestCustomer() {
        return CustomerFactory.createCustomer(
                1L,
                "John",
                "Doe",
                "john@gmail.com",
                "12345",
                "0712345678"
        );
    }

    @Test
    void testCreateValidOrder() {

        Order order = OrderFactory.createOrder(
                1001L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("250.00"),
                createTestCustomer(),
                null,
                null
        );

        assertNotNull(order);
        assertEquals(1001L, order.getOrderId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(new BigDecimal("250.00"), order.getTotalAmount());
        assertNotNull(order.getOrderItems());
        assertNotNull(order.getPayments());
    }

    @Test
    void testInvalidOrderId() {

        Order order = OrderFactory.createOrder(
                -1L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("250.00"),
                createTestCustomer(),
                null,
                null
        );

        assertNull(order);
    }

    @Test
    void testNullOrderDate() {

        Order order = OrderFactory.createOrder(
                1001L,
                null,
                OrderStatus.PENDING,
                new BigDecimal("250.00"),
                createTestCustomer(),
                null,
                null
        );

        assertNull(order);
    }

    @Test
    void testNullStatus() {

        Order order = OrderFactory.createOrder(
                1001L,
                LocalDateTime.now(),
                null,
                new BigDecimal("250.00"),
                createTestCustomer(),
                null,
                null
        );

        assertNull(order);
    }

    @Test
    void testInvalidTotalAmount() {

        Order order = OrderFactory.createOrder(
                1001L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("-50.00"),
                createTestCustomer(),
                null,
                null
        );

        assertNull(order);
    }

    @Test
    void testNullCustomer() {

        Order order = OrderFactory.createOrder(
                1001L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("250.00"),
                null,
                null,
                null
        );

        assertNull(order);
    }
}