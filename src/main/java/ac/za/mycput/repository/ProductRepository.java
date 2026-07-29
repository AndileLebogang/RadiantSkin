package ac.za.mycput.repository;

import ac.za.mycput.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBrand(String brand);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByStockQuantityGreaterThan(int quantity);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
