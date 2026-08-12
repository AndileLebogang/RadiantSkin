package ac.za.mycput.controller;

import ac.za.mycput.domain.BodyCareProduct;
import ac.za.mycput.factory.BodyCareProductFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class BodyCareProductControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/bodycare";

    private final BodyCareProduct product =
            BodyCareProductFactory.createBodyCareProduct(
                    1L,
                    "Body Lotion",
                    "Moisturising Lotion",
                    "Nivea",
                    new BigDecimal("120.00"),
                    20,
                    "image.jpg",
                    500,
                    "Dry Skin"
            );

    @Test
    void a_create() {

        ResponseEntity<BodyCareProduct> response =
                restTemplate.postForEntity(
                        BASE_URL + "/create",
                        product,
                        BodyCareProduct.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void b_read() {

        ResponseEntity<BodyCareProduct> response =
                restTemplate.getForEntity(
                        BASE_URL + "/read/" + product.getProductId(),
                        BodyCareProduct.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        System.out.println(response.getBody());
    }

    @Test
    void c_update() {

        BodyCareProduct updated = new BodyCareProduct.Builder()
                .copy(product)
                .setSkinConcern("Sensitive Skin")
                .build();

        restTemplate.put(
                BASE_URL + "/update",
                updated
        );

        ResponseEntity<BodyCareProduct> response =
                restTemplate.getForEntity(
                        BASE_URL + "/read/" + updated.getProductId(),
                        BodyCareProduct.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        System.out.println(response.getBody());
    }

    @Test
    void d_delete() {

        restTemplate.delete(
                BASE_URL + "/delete/" + product.getProductId()
        );

        System.out.println("Delete test completed.");
    }

    @Test
    void e_getAll() {

        ResponseEntity<List<BodyCareProduct>> response =
                restTemplate.exchange(
                        BASE_URL + "/getAll",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BodyCareProduct>>() {
                        });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        response.getBody().forEach(System.out::println);
    }

    @Test
    void f_findByBrand() {

        ResponseEntity<List<BodyCareProduct>> response =
                restTemplate.exchange(
                        BASE_URL + "/brand/Nivea",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BodyCareProduct>>() {
                        });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        if (response.getBody() != null) {
            response.getBody().forEach(System.out::println);
        }
    }

    @Test
    void g_search() {

        ResponseEntity<List<BodyCareProduct>> response =
                restTemplate.exchange(
                        BASE_URL + "/search/Lotion",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BodyCareProduct>>() {
                        });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        if (response.getBody() != null) {
            response.getBody().forEach(System.out::println);
        }
    }

    @Test
    void h_findInStock() {

        ResponseEntity<List<BodyCareProduct>> response =
                restTemplate.exchange(
                        BASE_URL + "/stock",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BodyCareProduct>>() {
                        });

        assertEquals(HttpStatus.OK, response.getStatusCode());

        if (response.getBody() != null) {
            response.getBody().forEach(System.out::println);
        }
    }
}