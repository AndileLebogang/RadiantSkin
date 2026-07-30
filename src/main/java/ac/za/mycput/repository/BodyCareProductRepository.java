package ac.za.mycput.repository;

import ac.za.mycput.domain.BodyCareProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodyCareProductRepository extends JpaRepository<BodyCareProduct, Long> {

    List<BodyCareProduct> findByBrand(String brand);

    List<BodyCareProduct> findByNameContainingIgnoreCase(String keyword);

    List<BodyCareProduct> findByStockQuantityGreaterThan(int quantity);
}
