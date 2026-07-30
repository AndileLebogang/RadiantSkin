/* HairCareProductController.java
     Author: Samkelo Mahlangu (230064019) */

package ac.za.mycput.controller;

import ac.za.mycput.domain.HairCareProduct;
import ac.za.mycput.service.IHairCareProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/haircare")
public class HairCareProductController {

    private final IHairCareProductService service;

    @Autowired
    public HairCareProductController(IHairCareProductService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public HairCareProduct create(@RequestBody HairCareProduct product) {
        return service.create(product);
    }

    @GetMapping("/read/{id}")
    public HairCareProduct read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public HairCareProduct update(@RequestBody HairCareProduct product) {
        return service.update(product);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/getAll")
    public List<HairCareProduct> getAll() {
        return service.getAll();
    }

    @GetMapping("/brand/{brand}")
    public List<HairCareProduct> findByBrand(@PathVariable String brand) {
        return service.findByBrand(brand);
    }

    @GetMapping("/search")
    public List<HairCareProduct> search(@RequestParam String keyword) {
        return service.searchByName(keyword);
    }

    @GetMapping("/inStock")
    public List<HairCareProduct> findInStock() {
        return service.findInStock();
    }
}
