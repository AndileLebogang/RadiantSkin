package ac.za.mycput.controller;

import ac.za.mycput.domain.*;
        import ac.za.mycput.repository.CustomerRepository;
import ac.za.mycput.repository.OrderRepository;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.IBodyCareProductService;
import ac.za.mycput.service.IHairCareProductService;
import ac.za.mycput.service.IProductService;
import ac.za.mycput.service.ISkinCareProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/admin-portal")
public class AdminPortalController {

    public record PaymentRow(Long paymentId, double amount, String method, String status, String reference) { }
    public record OrderRow(Long orderId, String date, String status, BigDecimal total, String customerName,
                           String email, String address, List<String> items, List<PaymentRow> payments) { }
    public record ProductRow(Long productId, String category, String name, String description, String brand,
                             BigDecimal price, int stock, String imageUrl, int volumeMl, String extra) { }
    public record ProductInput(String category, String name, String description, String brand,
                               BigDecimal price, int stock, String imageUrl, int volumeMl, String extra) { }
    public record CustomerRow(Long userId, String name, String email, String phone) { }
    public record StatusChange(String status) { }

    private final OrderRepository orders;
    private final CustomerRepository customers;
    private final AccountSession sessions;
    private final IProductService productService;
    private final ISkinCareProductService skinService;
    private final IHairCareProductService hairService;
    private final IBodyCareProductService bodyService;

    public AdminPortalController(OrderRepository orders, CustomerRepository customers,
                                 AccountSession sessions, IProductService productService,
                                 ISkinCareProductService skinService, IHairCareProductService hairService,
                                 IBodyCareProductService bodyService) {
        this.orders = orders;
        this.customers = customers;
        this.sessions = sessions;
        this.productService = productService;
        this.skinService = skinService;
        this.hairService = hairService;
        this.bodyService = bodyService;
    }

    private void requireAdmin(HttpServletRequest request) {
        User user = sessions.current(request);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Log in as an admin");
        if (user.getRole() != Role.ADMIN) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admins only");
    }

    private String nz(String value) {
        return value == null ? "" : value;
    }

    // ---------- Orders and payments ----------

    @GetMapping("/order-statuses")
    public List<String> orderStatuses(HttpServletRequest request) {
        requireAdmin(request);
        return Arrays.stream(OrderStatus.values()).map(Enum::name).toList();
    }

    @GetMapping("/payment-statuses")
    public List<String> paymentStatuses(HttpServletRequest request) {
        requireAdmin(request);
        return Arrays.stream(PaymentStatus.values()).map(Enum::name).toList();
    }

    @GetMapping("/orders")
    @Transactional(readOnly = true)
    public List<OrderRow> orders(HttpServletRequest request) {
        requireAdmin(request);
        return orders.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderId, Comparator.reverseOrder()))
                .map(this::row)
                .toList();
    }

    private OrderRow row(Order o) {
        CheckoutDetails d = o.getCheckoutDetails();
        String name = d == null ? "" : (nz(d.firstName) + " " + nz(d.lastName)).trim();
        String email = d == null ? "" : nz(d.email);
        String address = d == null ? "" :
                nz(d.street) + ", " + nz(d.city) + ", " + nz(d.province) + " " + nz(d.postalCode);
        List<String> items = o.getOrderItems().stream()
                .map(i -> (i.getProduct() == null ? "Product" : i.getProduct().getName()) + " × " + i.getQuantity())
                .toList();
        List<PaymentRow> pays = o.getPayments().stream()
                .map(p -> new PaymentRow(p.getPaymentId(), p.getAmount(),
                        p.getPaymentMethod() == null ? "" : p.getPaymentMethod().name(),
                        p.getPaymentStatus() == null ? "" : p.getPaymentStatus().name(),
                        nz(p.getTransactionReference())))
                .toList();
        return new OrderRow(o.getOrderId(),
                o.getOrderDate() == null ? "" : o.getOrderDate().toString(),
                o.getStatus() == null ? "" : o.getStatus().name(),
                o.getTotalAmount(), name, email, address, items, pays);
    }

    @PutMapping("/orders/{id}/status")
    @Transactional
    public void setOrderStatus(@PathVariable Long id, @RequestBody StatusChange body, HttpServletRequest request) {
        requireAdmin(request);
        Order order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        try {
            order.setStatus(OrderStatus.valueOf(body.status()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown order status");
        }
    }

    @PutMapping("/orders/{id}/payment-status")
    @Transactional
    public void setPaymentStatus(@PathVariable Long id, @RequestBody StatusChange body, HttpServletRequest request) {
        requireAdmin(request);
        Order order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (order.getPayments().isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This order has no payment");
        PaymentStatus status;
        try {
            status = PaymentStatus.valueOf(body.status());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown payment status");
        }
        for (Payment payment : order.getPayments()) payment.setPaymentStatus(status);
    }

    // ---------- Products: create, read, update, delete ----------

    private ProductRow productRow(Product p) {
        String category = "skin";
        String extra = "";
        if (p instanceof HairCareProduct h) {
            category = "hair";
            extra = nz(h.getHairConcern());
        } else if (p instanceof BodyCareProduct b) {
            category = "body";
            extra = nz(b.getSkinConcern());
        } else if (p instanceof SkinCareProduct s) {
            extra = nz(s.getUsageInstructions());
        }
        return new ProductRow(p.getProductId(), category, nz(p.getName()), nz(p.getDescription()),
                nz(p.getBrand()), p.getPrice(), p.getStockQuantity(), nz(p.getImageUrl()),
                p.getVolumeMl(), extra);
    }

    private void validate(ProductInput in) {
        if (in == null || in.name() == null || in.name().isBlank() || in.name().length() > 200
                || in.price() == null || in.price().signum() < 0
                || in.stock() < 0 || in.stock() > 100000
                || in.volumeMl() < 0 || in.volumeMl() > 100000
                || in.category() == null
                || !List.of("skin", "hair", "body").contains(in.category())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check the product details");
        }
    }

    private Product findProduct(Long id) {
        Product product = productService.read(id);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        return product;
    }

    @GetMapping("/products")
    @Transactional(readOnly = true)
    public List<ProductRow> products(HttpServletRequest request) {
        requireAdmin(request);
        return productService.getAll().stream()
                .map(this::productRow)
                .sorted(Comparator.comparing(ProductRow::productId))
                .toList();
    }

    @PostMapping("/products")
    @Transactional
    public ProductRow createProduct(@RequestBody ProductInput in, HttpServletRequest request) {
        requireAdmin(request);
        validate(in);
        Product saved;
        switch (in.category()) {
            case "hair" -> saved = hairService.create(new HairCareProduct.Builder()
                    .setName(in.name().trim()).setDescription(in.description()).setBrand(in.brand())
                    .setPrice(in.price()).setStockQuantity(in.stock()).setImageUrl(in.imageUrl())
                    .setVolumeMl(in.volumeMl()).setHairConcern(in.extra()).build());
            case "body" -> saved = bodyService.create(new BodyCareProduct.Builder()
                    .setName(in.name().trim()).setDescription(in.description()).setBrand(in.brand())
                    .setPrice(in.price()).setStockQuantity(in.stock()).setImageUrl(in.imageUrl())
                    .setVolumeMl(in.volumeMl()).setSkinConcern(in.extra()).build());
            default -> saved = skinService.create(new SkinCareProduct.Builder()
                    .setName(in.name().trim()).setDescription(in.description()).setBrand(in.brand())
                    .setPrice(in.price()).setStockQuantity(in.stock()).setImageUrl(in.imageUrl())
                    .setVolumeMl(in.volumeMl()).setUsageInstructions(in.extra()).build());
        }
        return productRow(saved);
    }

    @PutMapping("/products/{id}")
    @Transactional
    public ProductRow updateProduct(@PathVariable Long id, @RequestBody ProductInput in,
                                    HttpServletRequest request) {
        requireAdmin(request);
        validate(in);
        Product p = findProduct(id);
        p.setName(in.name().trim());
        p.setDescription(in.description());
        p.setBrand(in.brand());
        p.setPrice(in.price());
        p.setStockQuantity(in.stock());
        p.setImageUrl(in.imageUrl());
        p.setVolumeMl(in.volumeMl());
        Product saved;
        if (p instanceof HairCareProduct h) {
            h.setHairConcern(in.extra());
            saved = hairService.update(h);
        } else if (p instanceof BodyCareProduct b) {
            b.setSkinConcern(in.extra());
            saved = bodyService.update(b);
        } else if (p instanceof SkinCareProduct s) {
            s.setUsageInstructions(in.extra());
            saved = skinService.update(s);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown product type");
        }
        return productRow(saved);
    }

    @DeleteMapping("/products/{id}")
    @Transactional
    public void deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        requireAdmin(request);
        Product p = findProduct(id);
        // Deleting would also delete old order lines, so refuse if it was ever ordered.
        if (!p.getOrderItems().isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This product has been ordered before");
        if (p instanceof HairCareProduct) hairService.delete(id);
        else if (p instanceof BodyCareProduct) bodyService.delete(id);
        else skinService.delete(id);
    }

    // ---------- Customers ----------

    @GetMapping("/customers")
    @Transactional(readOnly = true)
    public List<CustomerRow> customers(HttpServletRequest request) {
        requireAdmin(request);
        return customers.findAll().stream()
                .map(c -> new CustomerRow(c.getUserId(),
                        (nz(c.getFirstName()) + " " + nz(c.getLastName())).trim(),
                        nz(c.getEmail()), nz(c.getPhoneNumber())))
                .toList();
    }
}