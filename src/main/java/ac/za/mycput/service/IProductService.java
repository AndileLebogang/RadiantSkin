package ac.za.mycput.service;

import ac.za.mycput.domain.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Store-wide catalog operations that apply across every product category
 * (skin care, hair care, body care). Category-specific behaviour lives in
 * ISkinCareProductService / IHairCareProductService / IBodyCareProductService.
 */
public interface IProductService extends IService<Product, Long> {

    List<Product> findByBrand(String brand);

    List<Product> searchByName(String keyword);

    List<Product> findInStock();

    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    boolean isInStock(Long productId, int requestedQuantity);

    Product reduceStock(Long productId, int quantity);

    Product restock(Long productId, int quantity);
}
