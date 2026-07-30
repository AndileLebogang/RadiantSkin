package ac.za.mycput.service;
/*
/Name: Siphokazi Malingatshoni
/student no: 222868708
 */
import ac.za.mycput.domain.Review;
import ac.za.mycput.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }
    @Override
    public Review create(Review review) {
        return repository.save(review);
    }

    @Override
    public Optional<Review> read(Long id) {
        return repository.findById(id);
    }

    @Override
    public Review update(Review review) {
        return repository.save(review);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Review> getAll() {
        return repository.findAll();
    }
}


