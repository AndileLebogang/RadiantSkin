package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.PaymentMethod;

public interface ICheckoutService {

    /**
     * Converts everything currently in the customer's cart into an Order:
     * builds the OrderItems, reduces product stock, records a Payment,
     * and empties the cart. This is the operation that ties the Cart,
     * Order, OrderItem, Payment and Product services together.
     */
    Order checkout(Customer customer, PaymentMethod paymentMethod);
}
