package ac.za.mycput.service;

/*
//Name & Surname: Siphokazi Malingatshoni
//student number: 222868708
 */

import ac.za.mycput.domain.Review;
import ac.za.mycput.repository.AddressRepository;
import ac.za.mycput.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewService  implements IReviewService{

    rivate final ReviewRepository repo;

    @Autowired
    public ReviewService(ReviewRepository repo){
        this.repo=repo;
    }

    @Override
    public Review create(Review review){
        return this.repo.save(review);
    }

    @Override
    public Review read(Long id){
        return this.repo.findById(id).orElse(null);
    }

    @Override
    public Review update(Review review){
        return this.repo.save(review);
    }

    @Override
    public boolean delete(Long id){
        this.repo.deleteById(id);
        return true;
    }

    @Override
    public list<Review> getAll(){
        return this.repo.findAll();
    }

}



}
