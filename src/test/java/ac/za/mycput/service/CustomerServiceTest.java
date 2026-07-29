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

    private static Customer customer;

    @Test
    void a_create() {

        customer = CustomerFactory.createCustomer(
                "John",
                "Doe",
                "john@gmail.com",
                "Password123",
                "0821234567"
        );

        customer = service.create(customer);

        assertNotNull(customer);
        assertNotNull(customer.getUserId());

        System.out.println(customer);
    }

    @Test
    void b_read() {

        Customer read = service.read(customer.getUserId());

        assertNotNull(read);
        assertEquals(customer.getUserId(), read.getUserId());

        System.out.println(read);
    }

    @Test
    void c_update() {

        Customer updated = new Customer.Builder()
                .copy(customer)
                .setPhoneNumber("0831234567")
                .build();

        updated = service.update(updated);

        customer = updated;

        assertEquals("0831234567", updated.getPhoneNumber());

        System.out.println(updated);
    }

    @Test
    void d_delete() {

        boolean success = service.delete(customer.getUserId());

        assertTrue(success);

        Customer deleted = service.read(customer.getUserId());

        assertNull(deleted);

        System.out.println("Deleted");
    }

    @Test
    void e_getAll() {

        List<Customer> customers = service.getAll();

        assertNotNull(customers);

        customers.forEach(System.out::println);
    }
}