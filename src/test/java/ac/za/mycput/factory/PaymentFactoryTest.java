//package ac.za.mycput.factory;
//
//import ac.za.mycput.domain.Payment;
//import ac.za.mycput.domain.PaymentMethod;
//import ac.za.mycput.domain.PaymentStatus;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PaymentFactoryTest {
//
//    @Test
//    void testCreateValidPayment() {
//
//        Payment payment = PaymentFactory.createPayment(
//                1001L,
//                500.0,
//                PaymentStatus.SUCCESSFUL,
//                PaymentMethod.CARD,
//                "TXN12345"
//        );
//
//        assertNotNull(payment);
//        assertEquals(1001L, payment.getPaymentID());
//        assertEquals(500.0, payment.getAmount());
//        assertEquals("TXN12345", payment.getTransactionReference());
//    }
//
//    @Test
//    void testInvalidPaymentId() {
//
//        Payment payment = PaymentFactory.createPayment(
//                -1L,
//                500.0,
//                PaymentStatus.SUCCESSFUL,
//                PaymentMethod.CARD,
//                "TXN12345"
//        );
//
//        assertNull(payment);
//    }
//
//    @Test
//    void testInvalidAmount() {
//
//        Payment payment = PaymentFactory.createPayment(
//                1001L,
//                -200.0,
//                PaymentStatus.SUCCESSFUL,
//                PaymentMethod.CARD,
//                "TXN12345"
//        );
//
//        assertNull(payment);
//    }
//
//    @Test
//    void testInvalidTransactionReference() {
//
//        Payment payment = PaymentFactory.createPayment(
//                1001L,
//                500.0,
//                PaymentStatus.SUCCESSFUL,
//                PaymentMethod.CARD,
//                ""
//        );
//
//        assertNull(payment);
//    }
//}