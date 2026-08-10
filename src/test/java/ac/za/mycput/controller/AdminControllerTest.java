package ac.za.mycput.controller;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.factory.AdminFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AdminControllerTest {

    private static Admin admin;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/admin";

    @BeforeAll
    static void setup() {

        admin = AdminFactory.createAdmin(
                1L,
                "Hazel",
                "Smith",
                "hazel@gmail.com",
                "Password123",
                "EMP001"
        );
    }

    @Test
    void a_create() {

        String url = BASE_URL + "/create";

        ResponseEntity<Admin> postResponse =
                this.restTemplate.postForEntity(url, admin, Admin.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        admin = postResponse.getBody();

        System.out.println(admin);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + admin.getUserId();

        ResponseEntity<Admin> response =
                this.restTemplate.getForEntity(url, Admin.class);

        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void c_update() {

        Admin updated = new Admin.Builder()
                .copy(admin)
                .setFirstName("Updated")
                .build();

        String url = BASE_URL + "/update";

        this.restTemplate.postForEntity(url, updated, Admin.class);

        ResponseEntity<Admin> response =
                this.restTemplate.getForEntity(
                        BASE_URL + "/read/" + updated.getUserId(),
                        Admin.class);

        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<Admin[]> response =
                this.restTemplate.getForEntity(url, Admin[].class);

        assertNotNull(response.getBody());

        for (Admin a : response.getBody()) {
            System.out.println(a);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + admin.getUserId();

        this.restTemplate.delete(url);

        System.out.println("Deleted Admin with ID: " + admin.getUserId());
    }
}