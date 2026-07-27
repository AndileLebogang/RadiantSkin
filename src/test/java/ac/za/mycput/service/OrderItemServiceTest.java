/* OrderItemServiceTest.java

     OrderItemService test class

     Author: La-eeq Lewis (240696255)

     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.Product;
import ac.za.mycput.factory.CustomerFactory;
import ac.za.mycput.factory.HairCareFactory;
import ac.za.mycput.factory.OrderFactory;
import ac.za.mycput.factory.OrderItemFactory;
import ac.za.mycput.repository.OrderItemRepository;
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
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository repo;

    @InjectMocks
    private OrderItemService orderItemService;

    private Order testOrder;
    private Product testProduct;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        Customer testCustomer = CustomerFactory.createCustomer(
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

        testProduct = HairCareFactory.createHairCareProduct(
                "Argan Oil Shampoo",
                "Nourishing shampoo for dry hair",
                "RadiantSkin",
                new BigDecimal("120.00"),
                50,
                "https://example.com/image.jpg",
                250,
                "Dryness"
        );

        testOrderItem = OrderItemFactory.createOrderItem(
                2001L,
                2,
                new BigDecimal("120.00"),
                testOrder,
                testProduct
        );
    }

    @Test
    void testCreate() {
        when(repo.save(testOrderItem)).thenReturn(testOrderItem);

        OrderItem result = orderItemService.create(testOrderItem);

        assertNotNull(result);
        assertEquals(testOrderItem.getOrderItemId(), result.getOrderItemId());
        verify(repo, times(1)).save(testOrderItem);
    }

    @Test
    void testRead() {
        when(repo.findById(2001L)).thenReturn(Optional.of(testOrderItem));

        OrderItem result = orderItemService.read(2001L);

        assertNotNull(result);
        assertEquals(2001L, result.getOrderItemId());
        verify(repo, times(1)).findById(2001L);
    }

    @Test
    void testReadNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        OrderItem result = orderItemService.read(999L);

        assertNull(result);
        verify(repo, times(1)).findById(999L);
    }

    @Test
    void testUpdate() {
        when(repo.save(testOrderItem)).thenReturn(testOrderItem);

        OrderItem result = orderItemService.update(testOrderItem);

        assertNotNull(result);
        assertEquals(testOrderItem.getOrderItemId(), result.getOrderItemId());
        verify(repo, times(1)).save(testOrderItem);
    }

    @Test
    void testDelete() {
        boolean result = orderItemService.delete(2001L);

        assertTrue(result);
        verify(repo, times(1)).deleteById(2001L);
    }

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(testOrderItem));

        List<OrderItem> items = orderItemService.getAll();

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindByOrder() {
        when(repo.findByOrder(testOrder)).thenReturn(List.of(testOrderItem));

        List<OrderItem> items = orderItemService.findByOrder(testOrder);

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(repo, times(1)).findByOrder(testOrder);
    }

    @Test
    void testFindByProduct() {
        when(repo.findByProduct(testProduct)).thenReturn(List.of(testOrderItem));

        List<OrderItem> items = orderItemService.findByProduct(testProduct);

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(repo, times(1)).findByProduct(testProduct);
    }
}