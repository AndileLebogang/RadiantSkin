package ac.za.mycput.factory;

import ac.za.mycput.util.Helper;
import java.time.LocalDate;
import ac.za.mycput.domain.Review;
/*Name:Siphokazi Malingatshoni
*Student Number: 222868708
 */

public class ReviewFactory {

    public static Review createReview(Long reviewId, int rating, String comment, LocalDate reviewDate, Customer customer, Product product){
        if (!Helper.isValidId(reviewId)||
           rating<=0||
          Helper.isNullEmpty(comment)||
          reviewDate ==null||
          customer ==null||
          product==null||){
            return null;
        }

        return new Review.Builder()
                .setReviewId(reviewId)
                .setRating(rating)
                .setComment(comment)
                .setReviewDate(reviewDate)
                .setCustomer(customer)
                .setProduct(product)
                .build();

    }
}
