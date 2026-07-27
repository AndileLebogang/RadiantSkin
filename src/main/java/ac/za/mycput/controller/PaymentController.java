package ac.za.mycput.controller;

import ac.za.mycput.domain.Payment;
import ac.za.mycput.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Payment create(@RequestBody Payment payment) {
        return service.create(payment);
    }

    @GetMapping("/read/{paymentId}")
    public Payment read(@PathVariable Long paymentId) {
        return service.read(paymentId);
    }

    @PutMapping("/update")
    public Payment update(@RequestBody Payment payment) {
        return service.update(payment);
    }

    @DeleteMapping("/delete/{paymentId}")
    public boolean delete(@PathVariable Long paymentId) {
        return service.delete(paymentId);
    }

    @GetMapping("/getAll")
    public List<Payment> getAll() {
        return service.getAll();
    }
}
