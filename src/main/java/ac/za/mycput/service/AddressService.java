package ac.za.mycput.service;
/*
//Name & Surname: Siphokazi Malingatshoni
//student number: 222868708
 */

import ac.za.mycput.domain.Address;
import ac.za.mycput.repository.AddressRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService implements IAddressService {

    private final AddressRepository repo;

    @Autowired
    public AddressService(AddressRepository repo){
        this.repo =repo;
    }
    @Override
    public Address create(Address address){
        return this.repo.save(address);
    }

    @Override
    public Address read(Long id){
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Address update(Address address){
        return this.repo.save(address);
    }

    @Override
    public boolean delete(Long id){
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public List<Address> getAll(){
        return this.repo.findAll();
    }
}




}
