package ac.za.mycput.factory;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.HairCareProduct;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemFactoryTest {

    private Order createTestOrder() {
        Customer customer = CustomerFactory.createCustomer(
                1L,
                "John",
                "Doe",
                "john@gmail.com",
                "12345",
                "0712345678"
        );

        return OrderFactory.createOrder(
                1001L,
                LocalDateTime.now(),
                OrderStatus.PENDING,
                new BigDecimal("250.00"),
                customer,
                null,
                null
        );
    }

    private Product createTestProduct() {
        return HairCareFactory.createHairCareProduct(
                "Argan Oil Shampoo",
                "Nourishing shampoo for dry hair",
                "RadiantSkin",
                new BigDecimal("120.00"),
                50,
                "http://example.com/image.jpg",
                250,
                "Dryness"
        );
    }

    @Test
    void testCreateValidOrderItem() {

        OrderItem orderItem = OrderItemFactory.createOrderItem(
                2001L,
                2,
                new BigDecimal("120.00"),
                createTestOrder(),
                createTestProduct()
        );

        assertNotNull(orderItem);
        assertEquals(2001L, orderItem.getOrderItemId());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(new BigDecimal("120.00"), orderItem.getUnitPrice());
        assertEquals(new BigDecimal("240.00"), orderItem.getSubtotal());
    }

    @Test
    void testInvalidOrderItemId() {

        OrderItem orderItem = OrderItemFactory.createOrderItem(
                -1L,
                2,
                new BigDecimal("120.00"),
                createTestOrder(),
                createTestProduct()
        );

        assertNull(orderItem);
    }

    @Test
    void testInvalidQuantity() {

        OrderItem orderItem = OrderItemFactory.createOrderItem(
                2001L,
                0,
                new BigDecimal("120.00"),
                createTestOrder(),
                createTestProduct()
        );

        assertNull(orderItem);
    }

    @Test
    void testInvalidUnitPrice() {

        OrderItem orderItem = OrderItemFactory.createOrderItem(
                2001L,
                2,
                new BigDecimal("-10.00"),
                createTestOrder(),
                createTestProduct()
        );

        assertNull(orderItem);
    }

    @Test
    void testNullOrder() {

        OrderItem orderItem = OrderItemFactory.createOrderItem(
                2001L,
                2,
                new BigDecimal("120.00"),
                null,
                createTestProduct()
        );

        assertNull(orderItem);
    }

    @Test
    void testNullProduct() {

        OrderItem orderItem = OrderItemFactory.createOrderItem(
                2001L,
                2,
                new BigDecimal("120.00"),
                createTestOrder(),
                null
        );

        assertNull(orderItem);
    }
}