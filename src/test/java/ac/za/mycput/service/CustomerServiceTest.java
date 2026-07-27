package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
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
class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    private final Customer customer = CustomerFactory.createCustomer(
            3455L,
            "John",
            "Doe",
            "john@gmail.com",
            "Password123",
            "0821234567"
    );

    @Test
    void a_create() {
        Customer created = service.create(customer);
        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {
        Customer read = service.read(customer.getUserId());
        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {
        Customer updated = new Customer.Builder()
                .copy(customer)
                .setPhoneNumber("0831234567")
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        System.out.println("Updated: " + updated);
    }

    @Test
    void d_delete() {
        boolean deleted = service.delete(customer.getUserId());
        assertTrue(deleted);
        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {
        List<Customer> customers = service.getAll();
        assertNotNull(customers);
        customers.forEach(System.out::println);
    }
}