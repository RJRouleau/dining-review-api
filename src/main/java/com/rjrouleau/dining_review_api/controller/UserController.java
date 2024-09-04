package com.rjrouleau.dining_review_api.controller;

import com.rjrouleau.dining_review_api.model.User;
import com.rjrouleau.dining_review_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName) {
        List<User> user = userRepository.findByUserName(userName);
        if (user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.getFirst(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        // check for existing user with this name and return bad request if found.
        List<User> temp = userRepository.findByUserName(user.getUserName());
        if (!temp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
