package ac.za.mycput.factory;

/* HairCareFactoryTest.java

     HairCareFactoryTest POJO class

     Author: Samkelo Mahlangu (230064019)

     Date: 29 June 2026 */

import ac.za.mycput.domain.HairCareProduct;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class HairCareFactoryTest {

    private HairCareProduct hairCare;

    @Test()
    void createHairCareProduct() {

        hairCare = HairCareFactory.createHairCareProduct("Shampoo", "Hair shampoo for all hair types", "BrandX", new BigDecimal("19.99"), 100, "http://example.com/shampoo.jpg", 250, "Apply to wet hair, lather, and rinse thoroughly."
        );

        assertNotNull(hairCare);
        assertEquals("Shampoo", hairCare.getName());
        assertEquals("Hair shampoo for all hair types", hairCare.getDescription());
        assertEquals("BrandX", hairCare.getBrand());
        assertEquals(new BigDecimal("19.99"), hairCare.getPrice());
        assertEquals(100, hairCare.getStockQuantity());
        assertEquals("http://example.com/shampoo.jpg", hairCare.getImageUrl());
        assertEquals(250, hairCare.getVolumeMl());
        assertEquals("Apply to wet hair, lather, and rinse thoroughly.", hairCare.getHairConcern());
        System.out.println(hairCare);
    }

    @Test
    void createHairCareProductWithInvalidData() {
        HairCareProduct invalidHairCare = HairCareFactory.createHairCareProduct("", "", "", new BigDecimal("-10.00"), -5, "", -100, "");
        assertNull(invalidHairCare);
    }

    @Test
    void createHairCareProductWithNullData() {
        HairCareProduct nullHairCare = HairCareFactory.createHairCareProduct(null, null, null, null, 0, null, 0, null);
        assertNull(nullHairCare);
    }
}