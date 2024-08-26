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
        if (optionalRestaurant.isEmpty()){
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

    @GetMapping("/search")
    public ResponseEntity<Object> getRestaurantByZipcodeAllergyDesc(
            @RequestParam(name = "zipcode") String zipcode,
            @RequestParam(name = "allergy") String allergy
    ){
        // validate that the zipcode is formatted correctly.
        if (!zipcode.matches("\\d{5}")) {
            return new ResponseEntity<>("Invalid zipcode. Zipcode must be 5 digits.", HttpStatus.BAD_REQUEST);
        }
        // validate that the allergy is peanut, egg, or dairy.
        if (!(allergy.equalsIgnoreCase("peanut") ||
                allergy.equalsIgnoreCase("egg") ||
                allergy.equalsIgnoreCase("dairy"))) {
            return new ResponseEntity<>("Invalid allergy. Allergy must be peanut, egg, or dairy", HttpStatus.BAD_REQUEST);
        }
        // call the appropriate method for the allergy
        List<Restaurant> restaurants = switch (allergy.toLowerCase()) {
            case "peanut" ->
                    restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(zipcode, 0.f);
            case "egg" ->
                    restaurantRepository.findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(zipcode, 0.f);
            case "dairy" ->
                    restaurantRepository.findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(zipcode, 0.f);
            default -> null;
        };
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createRestaurant(@RequestBody Restaurant restaurant){
        List<Restaurant> sameNameAndZipRestaurants = restaurantRepository.findByNameAndZipcode(restaurant.getName(), restaurant.getZipcode());
        if (!sameNameAndZipRestaurants.isEmpty()){
            return new ResponseEntity<>("Bad Request: Restaurant name must be unique for a given zipcode.", HttpStatus.BAD_REQUEST);
        }
        // TODO: if scores are not null, calculate the overall score
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurantDetails) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()) {
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
        // TODO: update overall score
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Restaurant> deleteRestaurant(@PathVariable Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Restaurant restaurantTBD = optionalRestaurant.get();
        restaurantRepository.delete(restaurantTBD);
        return new ResponseEntity<>(restaurantTBD, HttpStatus.NO_CONTENT);
    }
}
