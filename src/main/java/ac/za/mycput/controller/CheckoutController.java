package ac.za.mycput.controller;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.service.CustomerService;
import ac.za.mycput.service.ICheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final ICheckoutService checkoutService;
    private final CustomerService customerService;

    @Autowired
    public CheckoutController(ICheckoutService checkoutService, CustomerService customerService) {
        this.checkoutService = checkoutService;
        this.customerService = customerService;
    }

    /**
     * Converts the given customer's cart into an order and takes payment.
     * Example: POST /checkout/5?paymentMethod=CARD
     */
    @PostMapping("/{customerId}")
    public Order checkout(@PathVariable Long customerId, @RequestParam PaymentMethod paymentMethod) {
        Customer customer = customerService.read(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("No customer found with id " + customerId);
        }
        return checkoutService.checkout(customer, paymentMethod);
    }
}
