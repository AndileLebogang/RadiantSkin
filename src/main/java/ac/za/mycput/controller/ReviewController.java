package ac.za.mycput.controller;

/*
// Name : Siphokazi Malingatshoni
// Student no :222868708
 */

import ac.za.mycput.domain.Review;
import ac.za.mycput.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping("/create")
    public Review create(@RequestBody Review review){
        return service.create(review);
    }
    @GetMapping("/read/{id}")
    public Review read(@PathVariable Long id){
        return service.read(id);
    }
    @GetMapping("/all")
    public List<Review>getAll(){
        return service.getAll();
    }
    @PutMapping("/update")
    public Review update(@RequestBody Review review ){
        return service.update(review);
    }
    @DeleteMapping("/delete{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
