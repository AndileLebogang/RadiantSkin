package ac.za.mycput.repository;

import ac.za.mycput.domain.HairCareProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HairCareProductRepository extends JpaRepository<HairCareProduct, Long> {

    List<HairCareProduct> findByBrand(String brand);

    List<HairCareProduct> findByNameContainingIgnoreCase(String keyword);

    List<HairCareProduct> findByStockQuantityGreaterThan(int quantity);
}
