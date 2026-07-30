package ac.za.mycput.service;
/*
/Name: Siphokazi Malingatshoni
/Student Number: 222868708
 */

import ac.za.mycput.domain.Review;
import java.util.List;
import java.util.Optional;


public interface ReviewService {

    Review create(Review review);
    Optional<Review>read(Long id);
    Review update(Review review);
    void delete(Long id);
    List<Review>getAll();

}
