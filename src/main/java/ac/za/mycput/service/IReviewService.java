package ac.za.mycput.service;

/*
//Name & Surname: Siphokazi Malingatshoni
//student number: 222868708
 */

import ac.za.mycput.domain.Review;
import java.util.List;

public interface IReviewService {

    Review create (Review review);

    Review read(Long id);

    Review update( Review review );

    boolean delete(Long id);

    List<Review>getAll();

}


