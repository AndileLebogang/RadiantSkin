package ac.za.mycput.repository;

import ac.za.mycput.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    List<Customer> findByPhoneNumber(String phoneNumber);

}
