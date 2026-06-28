/* HairCareService.java

     HairCareService POJO class

     Author: Samkelo Mahlangu (230064019)

     Date: 28 June 2026 */

package ac.za.mycput.factory;

import ac.za.mycput.domain.HairCareProduct;
import ac.za.mycput.util.Helper;

import java.math.BigDecimal;

public class HairCareFactory {
    public static HairCareProduct createHairCareProduct(String name, String description, String brand, BigDecimal price, int stockQuantity, String imageUrl, int volumeMl, String hairConcern) {
        if (!Helper.isValidString(name)
                || !Helper.isValidString(description)
                || !Helper.isValidString(brand)
                || !Helper.isValidPrice(price)
                || !Helper.isValidStockQuantity(stockQuantity)
                || !Helper.isValidString(imageUrl)
                || !Helper.isValidStockQuantity(volumeMl)
                || !Helper.isValidString(hairConcern)) {
            return null;
        }

        return new HairCareProduct.Builder()
                .setName(name)
                .setDescription(description)
                .setBrand(brand)
                .setPrice(price)
                .setStockQuantity(stockQuantity)
                .setImageUrl(imageUrl)
                .setVolumeMl(volumeMl)
                .setHairConcern(hairConcern)
                .build();
    }
}
