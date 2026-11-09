package ac.za.mycput.repository;

import ac.za.mycput.domain.Address;
import ac.za.mycput.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer(Customer customer);
}
