package ac.za.mycput.service;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.factory.AdminFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class AdminServiceTest {

    @Autowired
    private AdminService service;

    @Test
    void a_create() {

        Admin admin = AdminFactory.createAdmin(
                "Naledi",
                "Molefe",
                "naledi@gmail.com",
                "Password123",
                "EMP001"
        );

        Admin created = service.create(admin);

        assertNotNull(created);
        assertNotNull(created.getUserId());

        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {

        Admin admin = AdminFactory.createAdmin(
                "Naledi",
                "Molefe",
                "naledi2@gmail.com",
                "Password123",
                "EMP002"
        );

        admin = service.create(admin);

        Admin read = service.read(admin.getUserId());

        assertNotNull(read);
        assertEquals(admin.getUserId(), read.getUserId());

        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {

        Admin admin = AdminFactory.createAdmin(
                "Naledi",
                "Molefe",
                "naledi3@gmail.com",
                "Password123",
                "EMP003"
        );

        admin = service.create(admin);

        Admin updated = new Admin.Builder()
                .copy(admin)
                .setEmployeeNumber("EMP999")
                .build();

        updated = service.update(updated);

        assertNotNull(updated);
        assertEquals("EMP999", updated.getEmployeeNumber());

        System.out.println("Updated: " + updated);
    }

    @Test
    void d_delete() {

        Admin admin = AdminFactory.createAdmin(
                "Naledi",
                "Molefe",
                "naledi4@gmail.com",
                "Password123",
                "EMP004"
        );

        admin = service.create(admin);

        boolean deleted = service.delete(admin.getUserId());

        assertTrue(deleted);

        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {

        List<Admin> admins = service.getAll();

        assertNotNull(admins);
        assertFalse(admins.isEmpty());

        admins.forEach(System.out::println);
    }
}