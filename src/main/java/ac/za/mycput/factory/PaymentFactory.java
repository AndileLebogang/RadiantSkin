package ac.za.mycput.factory;

// Tebogo Makgato 230116086

import ac.za.mycput.domain.Payment;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.domain.PaymentStatus;
import ac.za.mycput.util.Helper;

public class PaymentFactory {

    public static Payment createPayment(Long paymentId, double amount,
                                        PaymentStatus paymentStatus,
                                        PaymentMethod paymentMethod,
                                        String transactionReference) {

        if (!Helper.isValidId(paymentId) ||
                !Helper.isValidAmount(amount) ||
                !Helper.isValidTransactionReference(transactionReference) ||
                paymentStatus == null ||
                paymentMethod == null) {
            return null;
        }

        return new Payment.Builder()
                .setPaymentId(paymentId)
                .setAmount(amount)
                .setPaymentStatus(paymentStatus)
                .setPaymentMethod(paymentMethod)
                .setTransactionReference(transactionReference)
                .build();
    }
}