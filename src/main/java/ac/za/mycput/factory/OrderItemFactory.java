/* OrderItemFactory.java

     OrderItemFactory POJO class

     Author: La-eeq Lewis (240696255)

     Date: 28 June 2026 */

package ac.za.mycput.factory;

import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.Product;
import ac.za.mycput.util.Helper;

import java.math.BigDecimal;

public class OrderItemFactory {

    public static OrderItem createOrderItem(Long orderItemId, int quantity,
                                            BigDecimal unitPrice, Order order,
                                            Product product) {

        if (!Helper.isValidId(orderItemId) ||
                quantity <= 0 ||
                !Helper.isValidPrice(unitPrice) ||
                order == null ||
                product == null) {
            return null;
        }

        return new OrderItem.Builder()
                .setOrderItemId(orderItemId)
                .setQuantity(quantity)
                .setUnitPrice(unitPrice)
                .setOrder(order)
                .setProduct(product)
                .build();
    }
}