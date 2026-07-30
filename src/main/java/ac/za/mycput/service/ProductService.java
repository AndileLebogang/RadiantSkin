package ac.za.mycput.service;

import ac.za.mycput.domain.Product;
import ac.za.mycput.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repo;

    @Autowired
    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product create(Product product) {
        return this.repo.save(product);
    }

    @Override
    public Product read(Long id) {
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Product update(Product product) {
        return this.repo.save(product);
    }

    @Override
    public boolean delete(Long id) {
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<Product> getAll() {
        return this.repo.findAll();
    }

    @Override
    public List<Product> findByBrand(String brand) {
        return this.repo.findByBrand(brand);
    }

    @Override
    public List<Product> searchByName(String keyword) {
        return this.repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> findInStock() {
        return this.repo.findByStockQuantityGreaterThan(0);
    }

    @Override
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return this.repo.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public boolean isInStock(Long productId, int requestedQuantity) {
        Product product = this.read(productId);
        return product != null && product.getStockQuantity() >= requestedQuantity;
    }

    @Override
    public Product reduceStock(Long productId, int quantity) {
        Product product = this.read(productId);
        if (product == null) {
            return null;
        }
        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock for product " + productId);
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        return this.repo.save(product);
    }

    @Override
    public Product restock(Long productId, int quantity) {
        Product product = this.read(productId);
        if (product == null) {
            return null;
        }
        product.setStockQuantity(product.getStockQuantity() + quantity);
        return this.repo.save(product);
    }
}
