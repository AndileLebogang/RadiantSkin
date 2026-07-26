package ac.za.mycput.service;
/*
//Name & Surname: Siphokazi Malingatshoni
//student number: 222868708
 */
import ac.za.mycput.domain.Address;
import java.util.List;

public interface IAddressService {

    Address create(Address address);

    Address read(Long id);

    Address update(Address address);

    boolean delete(Long id);

    List<Address> getAll();


}


