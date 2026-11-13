package ac.za.mycput.controller;

import ac.za.mycput.dto.OrderSummary;
import ac.za.mycput.service.StorefrontCheckoutService;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.StorefrontCartService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/checkout")
public class StorefrontCheckoutController {
    private final StorefrontCheckoutService checkout;
    private final AccountSession sessions;
    private final StorefrontCartService carts;

    public StorefrontCheckoutController(StorefrontCheckoutService checkout, AccountSession sessions,
                                        StorefrontCartService carts) {
        this.checkout = checkout;
        this.sessions = sessions;
        this.carts = carts;
    }

    @PostMapping("/place")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<OrderSummary> place(@RequestBody StorefrontCheckoutService.Request request,
                                             HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                checkout.place(request, sessions.customer(httpRequest), carts.current(httpRequest)));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> invalid(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("message", error.getReason()));
    }
}
