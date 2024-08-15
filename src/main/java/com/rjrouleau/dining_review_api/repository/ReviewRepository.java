package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.Review;
import com.rjrouleau.dining_review_api.model.Review.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByStatus(Status status);
    List<Review> findByRestaurantNameAndStatus(String restaurantName, Status status);
}
