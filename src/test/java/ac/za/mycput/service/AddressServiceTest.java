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
@org.junit.jupiter.api.TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AddressServiceTest {
    @Autowired private ac.za.mycput.repository.CustomerRepository fixtureCustomers;

    @org.junit.jupiter.api.BeforeAll
    void preparePersistentFixtures() {
        fixtureCustomers.saveAndFlush(customer);
    }


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
    }

    @Test
    void b_read() {
        Address read = service.read(address.getAddressId());

        assertNotNull(read);
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

    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(address.getAddressId());

        assertTrue(deleted);
    }

    @Test
    void e_getAll() {

        List<Address> addresses = service.getAll();

        assertNotNull(addresses);

    }
}