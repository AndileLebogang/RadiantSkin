package ac.za.mycput.service;

import ac.za.mycput.domain.Payment;
import ac.za.mycput.domain.PaymentMethod;
import ac.za.mycput.domain.PaymentStatus;
import ac.za.mycput.factory.PaymentFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class PaymentServiceTest {

    @Autowired
    private PaymentService service;

    private final Payment payment = PaymentFactory.createPayment(
            1L,
            85.00,
            PaymentStatus.PENDING,
            PaymentMethod.CARD,
            "TM185"
    );

    @Test
    void a_create() {
        Payment created = service.create(payment);
        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {
        Payment read = service.read(payment.getPaymentId());
        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {

        Payment updated = new Payment.Builder()
                .copy(payment)
                .setAmount(1000.00)
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        System.out.println("Updated: " + updated);
    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(payment.getPaymentId());

        assertTrue(deleted);
        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {

        List<Payment> payments = service.getAll();

        assertNotNull(payments);

        payments.forEach(System.out::println);
    }
}