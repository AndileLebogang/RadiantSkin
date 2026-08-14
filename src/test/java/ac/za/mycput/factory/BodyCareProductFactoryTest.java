package ac.za.mycput.factory;

import ac.za.mycput.domain.BodyCareProduct;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BodyCareProductFactoryTest {

    @Test
    void createBodyCareProduct() {

        BodyCareProduct product = BodyCareProductFactory.createBodyCareProduct(
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

        assertNotNull(product);
    }
}