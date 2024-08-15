package com.rjrouleau.dining_review_api.controller;

import com.rjrouleau.dining_review_api.model.Restaurant;
import com.rjrouleau.dining_review_api.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public Iterable<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant){
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Restaurant> deleteRestaurant(@PathVariable Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (!optionalRestaurant.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Restaurant restaurantTBD = optionalRestaurant.get();
        restaurantRepository.delete(restaurantTBD);
        return new ResponseEntity<>(restaurantTBD, HttpStatus.NO_CONTENT);
    }
}
