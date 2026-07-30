package ac.za.mycput.repository;

import ac.za.mycput.domain.SkinCareProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkinCareProductRepository extends JpaRepository<SkinCareProduct, Long> {

    List<SkinCareProduct> findByBrand(String brand);

    List<SkinCareProduct> findByNameContainingIgnoreCase(String keyword);

    List<SkinCareProduct> findByStockQuantityGreaterThan(int quantity);
}
