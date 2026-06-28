/* SkinCareService.java

     SkinCareService POJO class

     Author: Samkelo Mahlangu (230064019)

     Date: 28 June 2026 */


package ac.za.mycput.factory;

import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.util.Helper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SkinCareFactory {

    public static SkinCareProduct createSkinCareProduct(String name, String description, String brand, BigDecimal price, int stockQuantity, String imageUrl, int volumeMl, String usageInstructions) {
        if (!Helper.isValidString(name)
                || !Helper.isValidString(description)
                || !Helper.isValidString(brand)
                || !Helper.isValidPrice(price)
                || !Helper.isValidStockQuantity(stockQuantity)
                || !Helper.isValidString(imageUrl)
                || !Helper.isValidStockQuantity(volumeMl)
                || !Helper.isValidString(usageInstructions)) {
            return null;
        }

        return new SkinCareProduct.Builder()
                .setName(name)
                .setDescription(description)
                .setBrand(brand)
                .setPrice(price)
                .setStockQuantity(stockQuantity)
                .setImageUrl(imageUrl)
                .setVolumeMl(volumeMl)
                .setUsageInstructions(usageInstructions)
                .build();
    }
}
