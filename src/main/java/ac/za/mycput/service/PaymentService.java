package ac.za.mycput.service;

import ac.za.mycput.domain.Payment;
import ac.za.mycput.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository repo;

    @Autowired
    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    @Override
    public Payment create(Payment payment) {
        return this.repo.save(payment);
    }

    @Override
    public Payment read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        return this.repo.save(payment);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<Payment> getAll() {
        return this.repo.findAll();
    }
}
