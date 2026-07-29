package ac.za.mycput.repository;

import ac.za.mycput.domain.Payment;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByTransactionReference(String transactionReference);

    List<Payment> findByPaymentStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
}
