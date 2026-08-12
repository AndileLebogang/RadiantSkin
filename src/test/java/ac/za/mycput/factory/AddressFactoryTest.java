package ac.za.mycput.factory;

import ac.za.mycput.domain.Address;
import ac.za.mycput.domain.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
*Name: Siphokazi Malingatshoni
* Student Number: 222868708
 */
public class AddressFactoryTest {

    private Customer createTestCustomer() {
        return CustomerFactory.createCustomer(
                "Siphokazi",
                "Malingatshoni",
                "siphokazi@gmail.com",
                "Password123",
                "0821234567"
        );
    }

    @Test
    void testCreateAddress() {

        Address address = AddressFactory.createAddress(
                "12 Main Road",
                "Cape Town",
                "Western Cape",
                "8000",
                "South Africa",
                createTestCustomer()
        );

        assertNotNull(address);
        assertEquals("12 Main Road", address.getStreet());
        assertEquals("Cape Town", address.getCity());
    }

    @Test
    void testCreateAddressWithNullStreet() {

        Address address = AddressFactory.createAddress(
                null,
                "Cape Town",
                "Western Cape",
                "8000",
                "South Africa",
                createTestCustomer()
        );

        assertNull(address);
    }

    @Test
    void testCreateAddressWithEmptyCity() {

        Address address = AddressFactory.createAddress(
                "12 Main Road",
                "",
                "Western Cape",
                "8000",
                "South Africa",
                createTestCustomer()
        );

        assertNull(address);
    }

    @Test
    void testCreateAddressWithNullCustomer() {

        Address address = AddressFactory.createAddress(
                "12 Main Road",
                "Cape Town",
                "Western Cape",
                "8000",
                "South Africa",
                null
        );

        assertNull(address);
    }
}
