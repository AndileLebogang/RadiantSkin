/* OrderFactory.java

     OrderFactory POJO class

     Author: La-eeq Lewis (240696255)

     Date: 28 June 2026 */

package ac.za.mycput.factory;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.Payment;
import ac.za.mycput.util.Helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFactory {

    public static Order createOrder(Long orderId, LocalDateTime orderDate,
                                    OrderStatus status, BigDecimal totalAmount,
                                    Customer customer, List<OrderItem> orderItems,
                                    List<Payment> payments) {

        if (!Helper.isValidId(orderId) ||
                orderDate == null ||
                status == null ||
                !Helper.isValidPrice(totalAmount) ||
                customer == null) {
            return null;
        }

        Order.Builder builder = new Order.Builder()
                .setOrderId(orderId)
                .setOrderDate(orderDate)
                .setStatus(status)
                .setTotalAmount(totalAmount)
                .setCustomer(customer);

        if (orderItems != null) {
            builder.setOrderItems(orderItems);
        }

        if (payments != null) {
            builder.setPayments(payments);
        }

        return builder.build();
    }
}