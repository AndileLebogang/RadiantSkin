package ac.za.mycput.service;

import ac.za.mycput.domain.Payment;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.domain.PaymentStatus;

import java.util.List;

public interface IPaymentService extends IService<Payment, Long> {

    Payment findByTransactionReference(String transactionReference);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
}
