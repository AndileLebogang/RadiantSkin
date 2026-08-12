package ac.za.mycput.controller;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.factory.AdminFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AdminControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/admin";


    private static Admin admin;

    @Test
    void a_create() {

        Admin newAdmin = AdminFactory.createAdmin(
                "Naledi",
                "Molefe",
                "naledi@gmail.com",
                "Password123",
                "EMP001"
        );

        ResponseEntity<Admin> response = restTemplate.postForEntity(
                BASE_URL + "/create",
                newAdmin,
                Admin.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        admin = response.getBody();

        assertNotNull(admin);
        assertNotNull(admin.getUserId());

        System.out.println("Created: " + admin);
    }

    @Test
    void b_read() {

        assertNotNull(admin);
        assertNotNull(admin.getUserId());

        ResponseEntity<Admin> response = restTemplate.getForEntity(
                BASE_URL + "/read/" + admin.getUserId(),
                Admin.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Read: " + response.getBody());
    }

    @Test
    void c_update() {

        assertNotNull(admin);

        Admin updated = new Admin.Builder()
                .copy(admin)
                .setEmployeeNumber("EMP999")
                .build();

        HttpEntity<Admin> entity = new HttpEntity<>(updated);

        ResponseEntity<Admin> response = restTemplate.exchange(
                BASE_URL + "/update",
                HttpMethod.PUT,
                entity,
                Admin.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        admin = response.getBody();

        assertNotNull(admin);
        assertEquals("EMP999", admin.getEmployeeNumber());

        System.out.println("Updated: " + admin);
    }

    @Test
    void d_getAll() {

        ResponseEntity<List<Admin>> response = restTemplate.exchange(
                BASE_URL + "/getAll",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Admin>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        response.getBody().forEach(System.out::println);
    }

    @Test
    void e_delete() {

        assertNotNull(admin);

        restTemplate.delete(
                BASE_URL + "/delete/" + admin.getUserId()
        );

        ResponseEntity<Admin> response = restTemplate.getForEntity(
                BASE_URL + "/read/" + admin.getUserId(),
                Admin.class
        );


        assertTrue(
                response.getStatusCode() == HttpStatus.NOT_FOUND
                        || response.getBody() == null
        );

        System.out.println("Deleted Successfully");
    }
}