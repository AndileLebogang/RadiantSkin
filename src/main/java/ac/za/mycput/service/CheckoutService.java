package ac.za.mycput.service;

import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.Payment;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.domain.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CheckoutService implements ICheckoutService {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final IOrderService orderService;
    private final IOrderItemService orderItemService;
    private final IProductService productService;
    private final IPaymentService paymentService;

    @Autowired
    public CheckoutService(CartService cartService,
                            CartItemService cartItemService,
                            IOrderService orderService,
                            IOrderItemService orderItemService,
                            IProductService productService,
                            IPaymentService paymentService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.paymentService = paymentService;
    }

    @Override
    public Order checkout(Customer customer, PaymentMethod paymentMethod) {
        Cart cart = this.cartService.findByCustomer(customer);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout: cart is empty or does not exist for this customer");
        }

        BigDecimal total = this.cartService.getCartTotal(cart.getCartId());

        Order order = this.orderService.create(new Order.Builder()
                .setOrderDate(LocalDateTime.now())
                .setStatus(OrderStatus.PENDING)
                .setTotalAmount(total)
                .setCustomer(customer)
                .build());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem.Builder()
                    .setQuantity(cartItem.getQuantity())
                    .setUnitPrice(cartItem.getProduct().getPrice())
                    .setOrder(order)
                    .setProduct(cartItem.getProduct())
                    .build();
            this.orderItemService.create(orderItem);

            this.productService.reduceStock(cartItem.getProduct().getProductId(), cartItem.getQuantity());
        }

        this.paymentService.create(new Payment.Builder()
                .setPaymentId(System.currentTimeMillis())
                .setAmount(total.doubleValue())
                .setPaymentStatus(PaymentStatus.PENDING)
                .setPaymentMethod(paymentMethod)
                .setTransactionReference(UUID.randomUUID().toString())
                .setOrder(order)
                .build());

        for (CartItem cartItem : cart.getCartItems()) {
            this.cartItemService.delete(cartItem.getCartItemId());
        }

        return order;
    }
}
