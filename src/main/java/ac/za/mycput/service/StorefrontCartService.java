package ac.za.mycput.service;

import ac.za.mycput.domain.*;
import ac.za.mycput.repository.CartRepository;
import ac.za.mycput.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class StorefrontCartService {
    public static final String SESSION_CART = "radiantskin.cartId";
    public record Line(Long productId, int quantity) {}
    private final CartRepository carts;
    private final ProductRepository products;
    private final AccountSession sessions;
    private final EntityManager entities;

    public StorefrontCartService(CartRepository carts, ProductRepository products,
                                 AccountSession sessions, EntityManager entities) {
        this.carts = carts;
        this.products = products;
        this.sessions = sessions;
        this.entities = entities;
    }

    public Cart current(HttpServletRequest request) {
        Customer customer = sessions.customer(request);
        if (customer != null) entities.lock(customer, LockModeType.PESSIMISTIC_WRITE);
        var session = request.getSession(true);
        Cart guest = session.getAttribute(SESSION_CART) instanceof Long id
                ? entities.find(Cart.class, id, LockModeType.PESSIMISTIC_WRITE) : null;
        // A session cart must never grant access to another customer's bag.
        if (guest != null && guest.getCustomer() != null
                && (customer == null || !guest.getCustomer().getUserId().equals(customer.getUserId()))) guest = null;
        Cart cart = guest;
        if (customer != null) {
            Cart accountCart = carts.findByCustomer(customer);
            if (accountCart != null) {
                entities.lock(accountCart, LockModeType.PESSIMISTIC_WRITE);
                if (guest != null && !guest.getCartId().equals(accountCart.getCartId())) {
                    for (CartItem item : guest.getCartItems()) {
                        var existing = accountCart.getCartItems().stream().filter(i ->
                                i.getProduct().getProductId().equals(item.getProduct().getProductId())).findFirst();
                        if (existing.isPresent()) existing.get().setQuantity(Math.min(99,
                                existing.get().getQuantity() + item.getQuantity()));
                        else accountCart.getCartItems().add(new CartItem.Builder().setCart(accountCart)
                                .setProduct(item.getProduct()).setQuantity(item.getQuantity()).build());
                    }
                    guest.getCartItems().clear();
                }
                cart = accountCart;
            }
        }
        if (cart == null) cart = carts.save(new Cart.Builder().setCreatedDate(LocalDate.now()).build());
        if (customer != null) cart.setCustomer(customer);
        session.setAttribute(SESSION_CART, cart.getCartId());
        return cart;
    }

    public List<Line> read(HttpServletRequest request) { return lines(current(request)); }

    public List<Line> replace(HttpServletRequest request, List<Line> lines) {
        if (lines == null || lines.size() > 100) throw invalid();
        Cart cart = current(request);
        Set<Long> ids = new HashSet<>();
        List<CartItem> replacements = new ArrayList<>();
        for (Line line : lines) {
            if (line == null || line.productId() == null || line.quantity() < 1
                    || line.quantity() > 99 || !ids.add(line.productId())) throw invalid();
            Product product = products.findById(line.productId()).orElseThrow(this::invalid);
            // Existing out-of-stock items remain visible so customers can remove them.
            var existing = cart.getCartItems().stream().filter(i ->
                    i.getProduct().getProductId().equals(line.productId())).findFirst();
            if (line.quantity() > product.getStockQuantity()
                    && (existing.isEmpty() || line.quantity() > existing.get().getQuantity()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "There is not enough stock. Refresh your bag.");
            CartItem item = existing.orElseGet(() -> new CartItem.Builder().setCart(cart).setProduct(product).build());
            item.setQuantity(line.quantity());
            replacements.add(item);
        }
        cart.getCartItems().removeIf(item -> !ids.contains(item.getProduct().getProductId()));
        for (CartItem item : replacements) if (!cart.getCartItems().contains(item)) cart.getCartItems().add(item);
        carts.saveAndFlush(cart);
        return lines(cart);
    }

    private List<Line> lines(Cart cart) {
        return cart.getCartItems().stream().map(i -> new Line(i.getProduct().getProductId(), i.getQuantity())).toList();
    }
    private ResponseStatusException invalid() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check the products and quantities in your bag.");
    }
}
