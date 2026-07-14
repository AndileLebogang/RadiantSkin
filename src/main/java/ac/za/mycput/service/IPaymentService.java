package ac.za.mycput.service;

import ac.za.mycput.domain.Payment;

import java.util.List;

public interface IPaymentService extends IService<Payment, Long> {

    List<Payment> getAll();
}
