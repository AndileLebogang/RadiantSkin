package ac.za.mycput.controller;

/*
// Name : Siphokazi Malingatshoni
// Student no :222868708
 */

import ac.za.mycput.domain.Review;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.IReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final IReviewService service;
    private final AccountSession sessions;

    @Autowired
    public ReviewController(IReviewService service, AccountSession sessions) {
        this.service = service;
        this.sessions = sessions;
    }

    public record ReviewSubmission(Long productId, Integer rating, String comment) {}

    public record ReviewView(Long reviewId, int rating, String comment, LocalDate reviewDate) {
        public static ReviewView from(Review review) {
            return new ReviewView(review.getReviewId(), review.getRating(),
                    review.getComment(), review.getReviewDate());
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody ReviewSubmission submission, HttpServletRequest request) {
        var user = sessions.current(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Log in to post your review.");
        }
        if (!(user instanceof Customer customer)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customer accounts can post reviews.");
        }
        if (submission == null || submission.productId() == null || submission.rating() == null) {
            return ResponseEntity.badRequest().body("Choose a product and rating");
        }

        try {
            Review saved = service.addReview(customer.getUserId(), submission.productId(),
                    submission.rating(), submission.comment());
            return ResponseEntity.status(HttpStatus.CREATED).body(ReviewView.from(saved));
        } catch (IllegalArgumentException invalidReview) {
            return ResponseEntity.badRequest().body(invalidReview.getMessage());
        }
    }

    @PostMapping("/create")
    public Review create(@RequestBody Review review) {
        return service.create(review);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody Map<String, Object> request) {
        try {
            Long customerId = Long.valueOf(request.get("customerId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            int rating = Integer.parseInt(request.get("rating").toString());
            String comment = (String) request.get("comment");

            Review saved = service.addReview(customerId, productId, rating, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public Review read(@PathVariable Long id) {
        return service.read(id);
    }

    @GetMapping("/product/{productId}")
    public List<ReviewView> getByProduct(@PathVariable Long productId) {
        List<Review> reviews = service.findByProductId(productId);
        return reviews == null ? List.of() : reviews.stream().map(ReviewView::from).toList();
    }

    @GetMapping("/customer/{customerId}")
    public List<Review> getByCustomer(@PathVariable Long customerId) {
        return service.findByCustomerId(customerId);
    }

    @PutMapping("/update")
    public Review update(@RequestBody Review review) {
        return service.update(review);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/getAll")
    public List<Review> getAll() {
        return service.getAll();
    }
}
