/* SkinCareService.java
     Author: Samkelo Mahlangu (230064019)
     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.repository.SkinCareProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkinCareService implements ISkinCareProductService {

    private final SkinCareProductRepository repo;

    @Autowired
    public SkinCareService(SkinCareProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public SkinCareProduct create(SkinCareProduct product) {
        return this.repo.save(product);
    }

    @Override
    public SkinCareProduct read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public SkinCareProduct update(SkinCareProduct product) {
        return this.repo.save(product);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<SkinCareProduct> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<SkinCareProduct> findByBrand(String brand) {
        return this.repo.findByBrand(brand);
    }

    @Override
    public List<SkinCareProduct> searchByName(String keyword) {
        return this.repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<SkinCareProduct> findInStock() {
        return this.repo.findByStockQuantityGreaterThan(0);
    }
}
