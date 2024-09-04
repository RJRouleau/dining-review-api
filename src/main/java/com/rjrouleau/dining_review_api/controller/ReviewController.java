package com.rjrouleau.dining_review_api.controller;

import com.rjrouleau.dining_review_api.model.Review;
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

    public ReviewController(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public ResponseEntity<Review> getReviewById(Long id){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Review review = optionalReview.get();
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<Review>> getApprovedReviewsByRestaurantName(String restaurantName){
        List<Review> reviews = reviewRepository.findByRestaurantNameAndStatus(restaurantName, Review.Status.ACCEPTED);
        if (reviews.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Review>> getReviewsByUserName(String userName){
        List<Review> reviews = reviewRepository.findByUserName(userName);
        if (reviews.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Review>> getReviewsPendingApproval(){
        List<Review> reviews = reviewRepository.findByStatus(Review.Status.PENDING);
        if (reviews.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review){
        // TODO: validate review.commentary length, content
        Review savedReview = reviewRepository.save(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }
}
