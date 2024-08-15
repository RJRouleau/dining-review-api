package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    int countByNameAndZipcode(String name, String zipcode);
    List<Restaurant> findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(String zipcode, Integer peanutScore);

}
