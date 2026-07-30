package ac.za.mycput.controller;

import ac.za.mycput.domain.Product;
import ac.za.mycput.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Store-wide catalog browsing across every product category. Creating,
 * updating and deleting products happens through the category-specific
 * controllers (SkinCareProductController, HairCareProductController,
 * BodyCareProductController) since those know the concrete product type.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final IProductService service;

    @Autowired
    public ProductController(IProductService service) {
        this.service = service;
    }

    @GetMapping("/read/{id}")
    public Product read(@PathVariable Long id) {
        return service.read(id);
    }

    @GetMapping("/getAll")
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/brand/{brand}")
    public List<Product> findByBrand(@PathVariable String brand) {
        return service.findByBrand(brand);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return service.searchByName(keyword);
    }

    @GetMapping("/inStock")
    public List<Product> findInStock() {
        return service.findInStock();
    }

    @GetMapping("/priceRange")
    public List<Product> findByPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return service.findByPriceRange(min, max);
    }
}
