package com.rjrouleau.dining_review_api.controller;

import com.rjrouleau.dining_review_api.AppUtils;
import com.rjrouleau.dining_review_api.model.Restaurant;
import com.rjrouleau.dining_review_api.model.Review;
import com.rjrouleau.dining_review_api.repository.RestaurantRepository;
import com.rjrouleau.dining_review_api.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository; // Spring Beans are singletons by default, meaning this
                                                             // repository is the same instance as the
                                                             // restaurantRepository in RestaurantController.

    public ReviewController(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository){

        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review){
        // TODO: validate review.commentary length, content
        Review savedReview = reviewRepository.save(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Review review = optionalReview.get();
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantName}")
    public ResponseEntity<List<Review>> getApprovedReviewsByRestaurantName(@PathVariable String restaurantName){
        List<Review> reviews = reviewRepository.findByRestaurantNameAndStatus(restaurantName, Review.Status.ACCEPTED);
        if (reviews.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<Review>> getReviewsByUserName(@PathVariable String userName){
        List<Review> reviews = reviewRepository.findByUserName(userName);
        if (reviews.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Updates a review's scores and commentary. Status is changed to PENDING.
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReview(
            @PathVariable Long id,
            @RequestBody Review reviewDetails
    ){
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()){
            return new ResponseEntity<>("Review not found.", HttpStatus.NOT_FOUND);
        }

        Review review = reviewOptional.get();
        AppUtils.setIfNotNull(reviewDetails::getPeanutScore, review::setPeanutScore);
        AppUtils.setIfNotNull(reviewDetails::getEggScore, review::setEggScore);
        AppUtils.setIfNotNull(reviewDetails::getDairyScore, review::setDairyScore);
        AppUtils.setIfNotNull(reviewDetails::getCommentary, review::setCommentary);
        review.setStatus(Review.Status.PENDING);
        Review updatedReview = reviewRepository.save(review);

        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Review>> getReviewsPendingApproval(){
        List<Review> reviews = reviewRepository.findByStatus(Review.Status.PENDING);
        if (reviews.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Updates the status of a review and recalculates overall score if approved. Provide status as a string path
    // variable. If status is not accepted or rejected, review is set to pending.
    @PutMapping("/admin/{id}")
    public ResponseEntity<Object> updateReviewStatus(@PathVariable Long id, @RequestBody String status){
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()){
            return new ResponseEntity<>("Review not found.", HttpStatus.NOT_FOUND);
        }

        Review review = reviewOptional.get();

        Review.Status reviewStatus = switch (status.toLowerCase()){
            case "accepted" -> Review.Status.ACCEPTED;
            case "rejected" -> Review.Status.REJECTED;
            default -> Review.Status.PENDING;
        };

        review.setStatus(reviewStatus);
        Review updatedReview = reviewRepository.save(review);
        if (reviewStatus == Review.Status.ACCEPTED){
            try {
                updateRestaurantScores(review.getRestaurantId(), review.getRestaurantName());
            } catch (Exception e) {
                return new ResponseEntity<>("An error occurred while updating restaurant scores.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable Long id){
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()){
            return new ResponseEntity<>("Review not found.", HttpStatus.NOT_FOUND);
        }

        Review review = reviewOptional.get();
        reviewRepository.delete(review);

        return new ResponseEntity<>(review, HttpStatus.NO_CONTENT);
    }

    // Calculates the overall score as the average of available allergy scores.
    // Used as part of the review approval process.
    private void updateOverallScore(Restaurant restaurant) {
        Float peanutScore = restaurant.getPeanutScore() != null ? restaurant.getPeanutScore() : 0.f;
        Float eggScore = restaurant.getEggScore() != null ? restaurant.getEggScore() : 0.f;
        Float dairyScore = restaurant.getDairyScore() != null ? restaurant.getDairyScore() : 0.f;

        Float overallScore = (peanutScore + eggScore + dairyScore) / 3.f;
        restaurant.setOverallScore(overallScore);
    }

    // Calculates each individual score across all approved reviews for a restaurant.
    // Used as part of the review approval process.

    private void updateRestaurantScores(Long restaurantId, String restaurantName) throws Exception {
        List<Review> reviewList = reviewRepository.findByRestaurantNameAndStatus(restaurantName, Review.Status.ACCEPTED);
        if (reviewList.isEmpty()) {
            throw new Exception ("No reviews were found.");
        }

        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()){
            throw new Exception("Restaurant was not found.");
        }
        Restaurant restaurant = restaurantOptional.get();

        Float peanutSum = 0.f;
        Float eggSum = 0.f;
        Float dairySum = 0.f;

        for (Review r: reviewList) {
            peanutSum += r.getPeanutScore() != null ? r.getPeanutScore() : 0.f;
            eggSum += r.getEggScore() != null ? r.getEggScore() : 0.f;
            dairySum += r.getDairyScore() != null ? r.getDairyScore() : 0.f;
        }

        restaurant.setPeanutScore(peanutSum / reviewList.size());
        restaurant.setEggScore(eggSum / reviewList.size());
        restaurant.setDairyScore(dairySum / reviewList.size());
        updateOverallScore(restaurant);
    }
}
