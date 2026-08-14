package ac.za.mycput.factory;

import ac.za.mycput.domain.BodyCareProduct;
import ac.za.mycput.util.Helper;

import java.math.BigDecimal;

public class BodyCareProductFactory {

    public static BodyCareProduct createBodyCareProduct(
            Long productId,
            String name,
            String description,
            String brand,
            BigDecimal price,
            int stockQuantity,
            String imageUrl,
            int volumeMl,
            String skinConcern) {

        if (!Helper.isValidId(productId) ||
                Helper.isNullEmpty(name) ||
                Helper.isNullEmpty(description) ||
                Helper.isNullEmpty(brand) ||
                price == null ||
                stockQuantity < 0 ||
                Helper.isNullEmpty(imageUrl) ||
                volumeMl <= 0 ||
                Helper.isNullEmpty(skinConcern)) {

            return null;
        }

        return new BodyCareProduct.Builder()
                .setProductId(productId)
                .setName(name)
                .setDescription(description)
                .setBrand(brand)
                .setPrice(price)
                .setStockQuantity(stockQuantity)
                .setImageUrl(imageUrl)
                .setVolumeMl(volumeMl)
                .setSkinConcern(skinConcern)
                .build();
    }
}
