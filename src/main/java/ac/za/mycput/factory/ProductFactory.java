package ac.za.mycput.factory;

import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.util.Helper;

import java.math.BigDecimal;

public class ProductFactory {

    public static SkinCareProduct createProduct(
            Long productId,
            String name,
            String description,
            String brand,
            BigDecimal price,
            int stockQuantity,
            String imageUrl,
            int volumeMl,
            String usageInstructions) {

        if (!Helper.isValidId(productId) ||
                Helper.isNullEmpty(name) ||
                Helper.isNullEmpty(description) ||
                Helper.isNullEmpty(brand) ||
                price == null ||
                price.compareTo(BigDecimal.ZERO) <= 0 ||
                stockQuantity < 0 ||
                Helper.isNullEmpty(imageUrl) ||
                volumeMl <= 0 ||
                Helper.isNullEmpty(usageInstructions)) {
            return null;
        }

        return new SkinCareProduct.Builder()
                .setProductId(productId)
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
