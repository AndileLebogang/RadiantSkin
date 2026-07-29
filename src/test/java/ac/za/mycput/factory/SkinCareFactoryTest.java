package ac.za.mycput.factory;

/* SkinCareFactoryTest.java

     SkinCareFactoryTest POJO class

     Author: Samkelo Mahlangu (230064019)

     Date: 29 June 2026 */

import ac.za.mycput.domain.SkinCareProduct;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class SkinCareFactoryTest {

    private SkinCareProduct skinCare;

    @Test
    void createSkinCareProduct() {

        skinCare = SkinCareFactory.createSkinCareProduct("Moisturizer", "Hydrating moisturizer for dry skin", "BrandY", new BigDecimal("29.99"), 50, "http://example.com/moisturizer.jpg", 100, "Apply to clean skin twice daily."
        );

        assertNotNull(skinCare);
        assertEquals("Moisturizer", skinCare.getName());
        assertEquals("Hydrating moisturizer for dry skin", skinCare.getDescription());
        assertEquals("BrandY", skinCare.getBrand());
        assertEquals(new BigDecimal("29.99"), skinCare.getPrice());
        assertEquals(50, skinCare.getStockQuantity());
        assertEquals("http://example.com/moisturizer.jpg", skinCare.getImageUrl());
        assertEquals(100, skinCare.getVolumeMl());
        assertEquals("Apply to clean skin twice daily.", skinCare.getUsageInstructions());
        System.out.println(skinCare);
    }

    @Test
    void createSkinCareProductWithInvalidData() {
        SkinCareProduct invalidSkinCare = SkinCareFactory.createSkinCareProduct("", "", "", new BigDecimal("-10.00"), -5, "", -100, "");
        assertNull(invalidSkinCare);
    }

    @Test
    void createSkinCareProductWithNullData() {
        SkinCareProduct nullSkinCare = SkinCareFactory.createSkinCareProduct(null, null, null, null, 0, null, 0, null);
        assertNull(nullSkinCare);
    }
}