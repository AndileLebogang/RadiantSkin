package ac.za.mycput.service;
/*
/Name: Siphokazi Malingatshoni
/student no: 222868708
 */
import ac.za.mycput.domain.Review;
import ac.za.mycput.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository repository;

    @Autowired
    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public Review create(Review review) {
        return repository.save(review);
    }

    @Override
    public Review read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Review update(Review review) {
        return repository.save(review);
    }

    @Override
    public boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Review> getAll() {
        return repository.findAll();
    }
}