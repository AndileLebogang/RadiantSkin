/* HairCareService.java
     Author: Samkelo Mahlangu (230064019)
     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.HairCareProduct;
import ac.za.mycput.repository.HairCareProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HairCareService implements IHairCareProductService {

    private final HairCareProductRepository repo;

    @Autowired
    public HairCareService(HairCareProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public HairCareProduct create(HairCareProduct product) {
        return this.repo.save(product);
    }

    @Override
    public HairCareProduct read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public HairCareProduct update(HairCareProduct product) {
        return this.repo.save(product);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<HairCareProduct> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<HairCareProduct> findByBrand(String brand) {
        return this.repo.findByBrand(brand);
    }

    @Override
    public List<HairCareProduct> searchByName(String keyword) {
        return this.repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<HairCareProduct> findInStock() {
        return this.repo.findByStockQuantityGreaterThan(0);
    }
}
