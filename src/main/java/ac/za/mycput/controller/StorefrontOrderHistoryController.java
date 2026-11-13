package ac.za.mycput.controller;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.dto.OrderSummary;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.StorefrontOrderHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StorefrontOrderHistoryController {
    private final AccountSession sessions;
    private final StorefrontOrderHistoryService history;

    public StorefrontOrderHistoryController(AccountSession sessions, StorefrontOrderHistoryService history) {
        this.sessions = sessions;
        this.history = history;
    }

    @GetMapping("/order/mine")
    public ResponseEntity<List<OrderSummary>> mine(HttpServletRequest request) {
        var user = sessions.current(request);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (!(user instanceof Customer customer))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(history.forCustomer(customer));
    }
}
