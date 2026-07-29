package ac.za.mycput.service;

/*
/Name: Siphokazi Malingatshoni
/student no: 222868708
 */
import ac.za.mycput.domain.Address;
import ac.za.mycput.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressService implements IAddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address create(Address address) {
        return repository.save(address);
    }

    @Override
    public Address read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Address update(Address address) {
        return repository.save(address);
    }

    @Override
    public boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Address> getAll() {
        return repository.findAll();
    }
}