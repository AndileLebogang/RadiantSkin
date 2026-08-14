package ac.za.mycput.controller;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.service.AdminService;
import ac.za.mycput.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AdminService adminService;
    private final CustomerService customerService;

    @Autowired
    public AuthController(AdminService adminService, CustomerService customerService) {
        this.adminService = adminService;
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Admin admin = adminService.login(email, password);
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException adminFailed) {

        }

        try {
            Customer customer = customerService.login(email, password);
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException customerFailed) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
