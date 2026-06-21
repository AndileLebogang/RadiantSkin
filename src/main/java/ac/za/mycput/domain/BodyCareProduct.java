/* BodyCareProduct.java

     BodyCareProduct POJO class

     Author: Samkelo Mahlangu (230064019)

     Date: 19 June 2026 */

package ac.za.mycput.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BODY")
public class BodyCareProduct extends Product {

    private String skinConcern;

    public BodyCareProduct() {
        super();
    }

    private BodyCareProduct(Builder builder) {
        this.productId = builder.productId;
        this.name = builder.name;
        this.description = builder.description;
        this.brand = builder.brand;
        this.price = builder.price;
        this.stockQuantity = builder.stockQuantity;
        this.imageUrl = builder.imageUrl;
        this.volumeMl = builder.volumeMl;
        this.skinConcern = builder.skinConcern;
    }

    public String getSkinConcern() {
        return skinConcern;
    }

    @Override
    public String toString() {
        return "BodyCareProduct{" +
                "skinConcern='" + skinConcern + '\'' +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", brand='" + brand + '\'' +
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
        private String skinConcern;

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

        public Builder setSkinConcern(String skinConcern) {
            this.skinConcern = skinConcern;
            return this;
        }

        public BodyCareProduct build() {
            return new BodyCareProduct(this);
        }
    }
}
