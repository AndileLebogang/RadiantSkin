package ac.za.mycput.service;

import ac.za.mycput.domain.BodyCareProduct;
import ac.za.mycput.factory.BodyCareProductFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class BodyCareProductServiceTest {

    @Autowired
    private BodyCareProductService service;

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

        BodyCareProduct created = service.create(product);

        assertNotNull(created);

        System.out.println(created);
    }

    @Test
    void b_read() {

        BodyCareProduct read = service.read(product.getProductId());

        assertNotNull(read);

        System.out.println(read);
    }

    @Test
    void c_update() {

        BodyCareProduct updated = new BodyCareProduct.Builder()
                .copy(product)
                .setSkinConcern("Sensitive Skin")
                .build();

        updated = service.update(updated);

        assertNotNull(updated);

        System.out.println(updated);
    }

    @Test
    void d_delete() {

        boolean deleted = service.delete(product.getProductId());

        assertTrue(deleted);

        System.out.println("Deleted Successfully");
    }

    @Test
    void e_getAll() {

        List<BodyCareProduct> products = service.getAll();

        assertNotNull(products);

        products.forEach(System.out::println);
    }
}