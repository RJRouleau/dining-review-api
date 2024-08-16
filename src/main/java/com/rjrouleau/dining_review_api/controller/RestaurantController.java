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

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (!optionalRestaurant.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalRestaurant.get(), HttpStatus.OK);
    }

    @GetMapping("/byzipcode/{zipcode}")
    public ResponseEntity<List<Restaurant>> getRestaurantByZipcode(@PathVariable String zipcode) {
        return new ResponseEntity<>(restaurantRepository.findByZipcode(zipcode), HttpStatus.OK);
    }
    @GetMapping("/bycity/{city}")
    public ResponseEntity<List<Restaurant>> getRestaurantByCity(@PathVariable String city) {
        return new ResponseEntity<>(restaurantRepository.findByCity(city), HttpStatus.OK);
    }

    @GetMapping("/bystate/{state}")
    public ResponseEntity<List<Restaurant>> getRestaurantByState(@PathVariable String state) {
        return new ResponseEntity<>(restaurantRepository.findByState(state), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant){
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurantDetails) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (!optionalRestaurant.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Restaurant restaurant = optionalRestaurant.get();

        if (restaurantDetails.getOverallScore() != null) {
            restaurant.setOverallScore(restaurantDetails.getOverallScore());
        }
        if (restaurantDetails.getPeanutScore() != null) {
            restaurant.setPeanutScore(restaurantDetails.getPeanutScore());
        }
        if (restaurantDetails.getEggScore() != null) {
            restaurant.setEggScore(restaurantDetails.getEggScore());
        }
        if (restaurantDetails.getDairyScore() != null) {
            restaurant.setDairyScore(restaurantDetails.getDairyScore());
        }
        if (restaurantDetails.getName() != null) {
            restaurant.setName(restaurantDetails.getName());
        }
        if (restaurantDetails.getCity() != null) {
            restaurant.setCity(restaurantDetails.getCity());
        }
        if (restaurantDetails.getState() != null) {
            restaurant.setState(restaurantDetails.getState());
        }
        if (restaurantDetails.getZipcode() != null) {
            restaurant.setZipcode(restaurantDetails.getZipcode());
        }
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
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


    /*
    * TODO:
    *  - helper function that validates a restaurant name is unique
    *  - use helper function in createRestaurant() to validate unique restaurant names at creation
    *  - function that finds all restaurants matching a given zipcode that also have at least one
    *    user-submitted score for a given allergy, returned in descending order.
    *
    *
    * */
}
