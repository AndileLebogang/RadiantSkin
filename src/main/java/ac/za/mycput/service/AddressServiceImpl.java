//package ac.za.mycput.service;
//
///*
///Name: Siphokazi Malingatshoni
///student no: 222868708
// */
//import ac.za.mycput.domain.Address;
//import ac.za.mycput.repository.AddressRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class AddressServiceImpl implements AddressService {
//
//    private final AddressRepository repository;
//
//    public AddressServiceImpl(AddressRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public Address create(Address address) {
//        return repository.save(address);
//    }
//
//    @Override
//    public Optional<Address> read(Long id) {
//        return repository.findById(id);
//
//    }
//    @Override
//    public Address update(Address address) {
//        return repository.save(address);
//    }
//
//    @Override
//    public void delete(Long id) {
//        repository.deleteById(id);
//    }
//    @Override
//    public List<Address> getAll() {
//        return repository.findAll();
//    }
//}
//
//
//
