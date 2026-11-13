package ac.za.mycput.controller;

import ac.za.mycput.service.StorefrontCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/storefront/cart")
public class StorefrontCartController {
    private final StorefrontCartService carts;
    public StorefrontCartController(StorefrontCartService carts) { this.carts = carts; }
    @GetMapping
    public ResponseEntity<List<StorefrontCartService.Line>> read(HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(carts.read(request));
    }
    @PutMapping
    public List<StorefrontCartService.Line> save(HttpServletRequest request,
                                               @RequestBody List<StorefrontCartService.Line> lines) {
        return carts.replace(request, lines);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> invalid(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("message", error.getReason()));
    }
}
