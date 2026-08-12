/* BodyCareProductController.java
     Author: Samkelo Mahlangu (230064019) */

package ac.za.mycput.controller;

import ac.za.mycput.domain.BodyCareProduct;
import ac.za.mycput.service.IBodyCareProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodycare")
public class BodyCareProductController {

    private final IBodyCareProductService service;

    @Autowired
    public BodyCareProductController(IBodyCareProductService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public BodyCareProduct create(@RequestBody BodyCareProduct product) {
        return service.create(product);
    }

    @GetMapping("/read/{id}")
    public BodyCareProduct read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public BodyCareProduct update(@RequestBody BodyCareProduct product) {
        return service.update(product);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/getAll")
    public List<BodyCareProduct> getAll() {
        return service.getAll();
    }

    @GetMapping("/brand/{brand}")
    public List<BodyCareProduct> findByBrand(@PathVariable String brand) {
        return service.findByBrand(brand);
    }

    @GetMapping("/search/{keyword}")
    public List<BodyCareProduct> search(@PathVariable String keyword) {
        return service.searchByName(keyword);
    }

    @GetMapping("/stock")
    public List<BodyCareProduct> findInStock() {
        return service.findInStock();
    }
}