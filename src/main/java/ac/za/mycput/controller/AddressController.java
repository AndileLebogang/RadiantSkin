package ac.za.mycput.controller;

/*
// Name : Siphokazi Malingatshoni
// Student no :222868708
 */

import ac.za.mycput.domain.Address;
import ac.za.mycput.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService service;

    @Autowired
    public AddressController(AddressService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Address create(@RequestBody Address address) {
        return service.create(address);
    }

    @GetMapping("/read/{id}")
    public Optional<Address> read(@PathVariable Long id) {
        return service.read(id);
    }

    @GetMapping("/getAll")
    public List<Address> getAll() {
        return service.getAll();
    }

    @PutMapping("/update")
    public Address update(@RequestBody Address address) {
        return service.update(address);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
