package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    int countByNameAndZipcode(String name, String zipcode);
    List<Restaurant> findByZipcode(String zipcode);
    List<Restaurant> findByCity(String city);
    List<Restaurant> findByState(String state);
    List<Restaurant> findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(String zipcode, Integer peanutScore);

}
