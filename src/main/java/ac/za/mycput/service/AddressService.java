package ac.za.mycput.service;
/*
/Name: Siphokazi Malingatshoni
/Student Number: 222868708
 */
import ac.za.mycput.domain.Address;
import java.util.List;
import java.util.Optional;

public interface AddressService {

    Address create(Address address);
    Optional<Address>read(Long id);
    Address update(Address address);
    void delete(Long id);
    List<Address>getAll();

}
