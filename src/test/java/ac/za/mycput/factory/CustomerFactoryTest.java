package ac.za.mycput.factory;

import ac.za.mycput.domain.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerFactoryTest {

    @Test
    void testCreateValidCustomer() {

        Customer customer = CustomerFactory.createCustomer(
                "John",
                "Doe",
                "john@gmail.com",
                "12345",
                "0712345678"
        );

        assertNotNull(customer);
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john@gmail.com", customer.getEmail());
    }

    @Test
    void testCreateInvalidEmailCustomer() {

        Customer customer = CustomerFactory.createCustomer(
                "John",
                "Doe",
                "invalidemail",
                "12345",
                "0712345678"
        );

        assertNull(customer);
    }

    @Test
    void testCreateInvalidPhoneCustomer() {

        Customer customer = CustomerFactory.createCustomer(
                "John",
                "Doe",
                "john@gmail.com",
                "12345",
                "123"
        );

        assertNull(customer);
    }
}




