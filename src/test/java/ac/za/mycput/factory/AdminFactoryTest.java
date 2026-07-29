package ac.za.mycput.factory;

import ac.za.mycput.domain.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminFactoryTest {

    @Test
    void createAdmin() {

        Admin admin = AdminFactory.createAdmin(
                "Naledi",
                "Molefe",
                "naledi.molefe@gmail.com",
                "Password123",
                "EMP001"
        );

        assertNotNull(admin);
        assertEquals("Naledi", admin.getFirstName());
        assertEquals("Molefe", admin.getLastName());
        assertEquals("naledi.molefe@gmail.com", admin.getEmail());
        assertEquals("Password123", admin.getPassword());
        assertEquals("EMP001", admin.getEmployeeNumber());
    }

    @Test
    void createAdminWithInvalidEmail() {

        Admin admin = AdminFactory.createAdmin(
                "Thabo",
                "Ndlovu",
                "thabondlovugmail.com",
                "Secure123",
                "EMP002"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithEmptyFirstName() {

        Admin admin = AdminFactory.createAdmin(
                "Katlego",
                "Khumalo",
                "ayanda.khumalo@gmail.com",
                "Pass123",
                "EMP003"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithEmptyLastName() {

        Admin admin = AdminFactory.createAdmin(
                "Boitumelo",
                "Tau",
                "boitumelo.modise@gmail.com",
                "Pass123",
                "EMP004"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithEmptyPassword() {

        Admin admin = AdminFactory.createAdmin(
                "Karabo",
                "Dlamini",
                "karabo.dlamini@gmail.com",
                "",
                "EMP005"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithEmptyEmployeeNumber() {

        Admin admin = AdminFactory.createAdmin(
                "Aphiwe",
                "Jacobs",
                "aphiwe.jacobs@gmail.com",
                "Password123",
                ""
        );

        assertNull(admin);
    }
}