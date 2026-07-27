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
import ac.za.mycput.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repo;

    @InjectMocks
    private OrderService orderService;

    private Customer testCustomer;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testCustomer = CustomerFactory.createCustomer(
                1L,
                "John",
                "Doe",
                "john@gmail.com",
                "password123",
                "0712345678"
        );

        testOrder = OrderFactory.createOrder(
                1001L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("250.00"),
                testCustomer,
                null,
                null
        );
    }

    @Test
    void testCreate() {
        when(repo.save(testOrder)).thenReturn(testOrder);

        Order result = orderService.create(testOrder);

        assertNotNull(result);
        assertEquals(testOrder.getOrderId(), result.getOrderId());
        verify(repo, times(1)).save(testOrder);
    }

    @Test
    void testRead() {
        when(repo.findById(1001L)).thenReturn(Optional.of(testOrder));

        Order result = orderService.read(1001L);

        assertNotNull(result);
        assertEquals(1001L, result.getOrderId());
        verify(repo, times(1)).findById(1001L);
    }

    @Test
    void testReadNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Order result = orderService.read(999L);

        assertNull(result);
        verify(repo, times(1)).findById(999L);
    }

    @Test
    void testUpdate() {
        when(repo.save(testOrder)).thenReturn(testOrder);

        Order result = orderService.update(testOrder);

        assertNotNull(result);
        assertEquals(testOrder.getOrderId(), result.getOrderId());
        verify(repo, times(1)).save(testOrder);
    }

    @Test
    void testDelete() {
        boolean result = orderService.delete(1001L);

        assertTrue(result);
        verify(repo, times(1)).deleteById(1001L);
    }

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(testOrder));

        List<Order> orders = orderService.getAll();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindByCustomer() {
        when(repo.findByCustomer(testCustomer)).thenReturn(List.of(testOrder));

        List<Order> orders = orderService.findByCustomer(testCustomer);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(repo, times(1)).findByCustomer(testCustomer);
    }

    @Test
    void testFindByStatus() {
        when(repo.findByStatus(OrderStatus.PENDING)).thenReturn(List.of(testOrder));

        List<Order> orders = orderService.findByStatus(OrderStatus.PENDING);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(repo, times(1)).findByStatus(OrderStatus.PENDING);
    }
}