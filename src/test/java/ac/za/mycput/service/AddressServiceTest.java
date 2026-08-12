package ac.za.mycput.service;

import ac.za.mycput.domain.Address;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.factory.AddressFactory;
import ac.za.mycput.factory.CustomerFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class AddressServiceTest {

    @Autowired
    private AddressService service;

    private final Customer customer = CustomerFactory.createCustomer(
            "Tebogo",
            "Makgato",
            "tebogo@gmail.com",
            "Password123",
            "0712345678"
    );

    private final Address address = AddressFactory.createAddress(
            "123 Main Street",
            "Cape Town",
            "Western Cape",
            "8001",
            "South Africa",
            customer
    );

    @Test
    void a_create() {
        Address created = service.create(address);

        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {
        Address read = service.read(address.getAddressId());

        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {

        Address updated = new Address.Builder()
                .copy(address)
                .setCity("Johannesburg")
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        assertEquals("Johannesburg", updated.getCity());

        System.out.println("Updated: " + updated);
    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(address.getAddressId());

        assertTrue(deleted);
        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {

        List<Address> addresses = service.getAll();

        assertNotNull(addresses);

        addresses.forEach(System.out::println);
    }
}