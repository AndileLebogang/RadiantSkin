/* SkinCareProductController.java
     Author: Samkelo Mahlangu (230064019) */

package ac.za.mycput.controller;

import ac.za.mycput.domain.SkinCareProduct;
import ac.za.mycput.service.ISkinCareProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skincare")
public class SkinCareProductController {

    private final ISkinCareProductService service;

    @Autowired
    public SkinCareProductController(ISkinCareProductService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public SkinCareProduct create(@RequestBody SkinCareProduct product) {
        return service.create(product);
    }

    @GetMapping("/read/{id}")
    public SkinCareProduct read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public SkinCareProduct update(@RequestBody SkinCareProduct product) {
        return service.update(product);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/getAll")
    public List<SkinCareProduct> getAll() {
        return service.getAll();
    }

    @GetMapping("/brand/{brand}")
    public List<SkinCareProduct> findByBrand(@PathVariable String brand) {
        return service.findByBrand(brand);
    }

    @GetMapping("/search")
    public List<SkinCareProduct> search(@RequestParam String keyword) {
        return service.searchByName(keyword);
    }

    @GetMapping("/inStock")
    public List<SkinCareProduct> findInStock() {
        return service.findInStock();
    }
}
