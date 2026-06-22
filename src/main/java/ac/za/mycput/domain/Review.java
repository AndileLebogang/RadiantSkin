package ac.za.mycput.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

/*
*Name: Siphokazi Malingatshoni
* Student Number:222868708
* Date: 2026/06/21
 */
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private int rating;
    private String comment;
    private LocalDate reviewDate;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Review() {
    }
    public Review (Builder builder){
        this.reviewId = builder.reviewId;
        this. rating = builder.rating;
        this.comment = builder.comment;
        this.reviewDate = builder.reviewDate;
        this.product = builder.product;
        this.customer = builder.customer;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public Product getProduct() {
        return product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static class Builder{
        private Long reviewId;
        private int rating;
        private String comment;
        private LocalDate reviewDate;
        private Product product;
        private Customer customer;

        public Builder setReviewId(Long reviewId){
            this.reviewId = reviewId;
            return this;
        }

        public Builder setRating(int rating){
            this.rating = rating;
            return this;
        }

        public Builder setComment(String comment){
            this.comment = comment;
            return this;
        }

        public Builder setReviewDate(LocalDate reviewDate){
            this.reviewDate = reviewDate;
            return this;
        }
        public Builder setProduct(Product product){
            this.product = product;
            return this;
        }
        public Builder setCustomer (Customer customer ){
            this.customer = customer;
            return this;
        }

        public Builder copy(Review review){
            this.reviewId = review.reviewId;
            this.rating = review.rating;
            this.comment = review.comment;
            this.reviewDate = review.reviewDate;
            this.product= review.product;
            this.customer = review.customer;
            return this;
        }

        public  Review build(){
         return new Review(this);
        }
    }
}
