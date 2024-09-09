package com.rjrouleau.dining_review_api.controller;

import com.rjrouleau.dining_review_api.AppUtils;
import com.rjrouleau.dining_review_api.model.User;
import com.rjrouleau.dining_review_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{userName}")
    public ResponseEntity<Object> getUserByUserName(@PathVariable String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()){
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    // Creates a new user and verifies that the userName is unique.
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user){
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isPresent()){
            return new ResponseEntity<Object>("Username is taken. Please choose a unique username.", HttpStatus.BAD_REQUEST);
        }

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // updates a user profile without changing the username. If a new username is provided, it is ignored and the
    // remaining fields are still updated.
    @PutMapping("/{userName}")
    public ResponseEntity<Object> updateUser(
            @PathVariable String userName,
            @RequestBody User userDetails
    ){
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()){
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        AppUtils.setIfNotNull(userDetails::getCity, user::setCity);
        AppUtils.setIfNotNull(userDetails::getState, user::setState);
        AppUtils.setIfNotNull(userDetails::getZipcode, user::setZipcode);
        AppUtils.setIfNotNull(userDetails::getPeanutAllergy, user::setPeanutAllergy);
        AppUtils.setIfNotNull(userDetails::getEggAllergy, user::setEggAllergy);
        AppUtils.setIfNotNull(userDetails::getDairyAllergy, user::setDairyAllergy);
        User updatedUser = userRepository.save(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userName){
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()){
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
        User userTBD = userOptional.get();
        userRepository.delete(userTBD);
        return new ResponseEntity<>(userTBD, HttpStatus.NO_CONTENT);
    }
}
