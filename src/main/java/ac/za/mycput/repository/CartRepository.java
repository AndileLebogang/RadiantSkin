package ac.za.mycput.repository;

// Lebogang Andile Mahlangu  230561454 //
import ac.za.mycput.domain.Cart;
import ac.za.mycput.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByCustomer(Customer customer);
    List<Cart> findByCreatedDate(java.time.LocalDate createdDate);

}