package ac.za.mycput.repository;
/*
/Name: Siphokazi Malingatshoni
/Student Number; 222868708
 */
import ac.za.mycput.domain.Address;
import ac.za.mycput.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

  public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findByCustomer(Customer customer);
}
