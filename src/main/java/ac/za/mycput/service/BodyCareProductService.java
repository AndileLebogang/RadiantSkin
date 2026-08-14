/* BodyCareService.java
     Author: Samkelo Mahlangu (230064019)
     Date: 12 July 2026 */

package ac.za.mycput.service;

import ac.za.mycput.domain.BodyCareProduct;
import ac.za.mycput.repository.BodyCareProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodyCareProductService implements IBodyCareProductService {

    private final BodyCareProductRepository repo;

    @Autowired
    public BodyCareProductService(BodyCareProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public BodyCareProduct create(BodyCareProduct product) {
        return this.repo.save(product);
    }

    @Override
    public BodyCareProduct read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public BodyCareProduct update(BodyCareProduct product) {
        return this.repo.save(product);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<BodyCareProduct> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<BodyCareProduct> findByBrand(String brand) {
        return this.repo.findByBrand(brand);
    }

    @Override
    public List<BodyCareProduct> searchByName(String keyword) {
        return this.repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<BodyCareProduct> findInStock() {
        return this.repo.findByStockQuantityGreaterThan(0);
    }
}
