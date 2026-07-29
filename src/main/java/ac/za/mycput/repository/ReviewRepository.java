package ac.za.mycput.repository;
/*
/Name : Siphokazi Malingatshoni
/Student Number: 222868708
 */
import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository <Review,Long>  {

 List<Review> findByCustomer(Customer customer);

 List<Review> findByProduct(Product product);
}
