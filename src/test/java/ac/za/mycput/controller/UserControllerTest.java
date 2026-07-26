package ac.za.mycput.controller;

import ac.za.mycput.domain.User;
import ac.za.mycput.factory.UserFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserControllerTest {

    private static User user;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/user";

    @BeforeAll
    static void setup() {

        user = UserFactory.createUser(
                1L,
                "Hazel",
                "Smith",
                "hazel@gmail.com",
                "Password123"
        );
    }

    @Test
    void a_create() {

        String url = BASE_URL + "/create";

        ResponseEntity<User> postResponse =
                this.restTemplate.postForEntity(url, user, User.class);

        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());

        user = postResponse.getBody();

        System.out.println(user);
    }

    @Test
    void b_read() {

        String url = BASE_URL + "/read/" + user.getUserId();

        ResponseEntity<User> response =
                this.restTemplate.getForEntity(url, User.class);

        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void c_update() {

        User updated = new User.Builder()
                .copy(user)
                .setFirstName("Updated")
                .build();

        String url = BASE_URL + "/update";

        ResponseEntity<User> response =
                this.restTemplate.postForEntity(url, updated, User.class);

        assertNotNull(response);
        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void d_getAll() {

        String url = BASE_URL + "/getAll";

        ResponseEntity<User[]> response =
                this.restTemplate.getForEntity(url, User[].class);

        assertNotNull(response.getBody());

        for (User u : response.getBody()) {
            System.out.println(u);
        }
    }

    @Test
    void e_delete() {

        String url = BASE_URL + "/delete/" + user.getUserId();

        this.restTemplate.delete(url);

        System.out.println("Deleted User with ID: " + user.getUserId());
    }
}
