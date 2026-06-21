/* HairCareProduct.java

     HairCareProduct POJO class

     Author: Samkelo Mahlangu (230064019)

     Date: 19 June 2026 */

package ac.za.mycput.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("HAIR")
public class HairCareProduct extends Product{

    private String hairConcern;

    public HairCareProduct() {
        super();
    }

    private HairCareProduct(Builder builder) {
        this.productId = builder.productId;
        this.name = builder.name;
        this.description = builder.description;
        this.brand = builder.brand;
        this.price = builder.price;
        this.stockQuantity = builder.stockQuantity;
        this.imageUrl = builder.imageUrl;
        this.volumeMl = builder.volumeMl;
        this.hairConcern = builder.hairConcern;
    }

    public String getHairConcern() {
        return hairConcern;
    }

    @Override
    public String toString() {
        return "HairCareProduct{" +
                "hairConcern='" + hairConcern + '\'' +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", volumeMl=" + volumeMl +
                '}';
    }

    public static class Builder {
        private Long productId;
        private String name;
        private String description;
        private String brand;
        private BigDecimal price;
        private int stockQuantity;
        private String imageUrl;
        private int volumeMl;
        private String hairConcern;

        public Builder setProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setBrand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setStockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setVolumeMl(int volumeMl) {
            this.volumeMl = volumeMl;
            return this;
        }

        public Builder setHairConcern(String hairConcern) {
            this.hairConcern = hairConcern;
            return this;
        }

        public Builder copy(HairCareProduct hairCareProduct) {
            this.productId = hairCareProduct.productId;
            this.name = hairCareProduct.name;
            this.description = hairCareProduct.description;
            this.brand = hairCareProduct.brand;
            this.price = hairCareProduct.price;
            this.stockQuantity = hairCareProduct.stockQuantity;
            this.imageUrl = hairCareProduct.imageUrl;
            this.volumeMl = hairCareProduct.volumeMl;
            this.hairConcern = hairCareProduct.hairConcern;
            return this;
        }

        public HairCareProduct build() {
            return new HairCareProduct(this);
        }
    }
}
