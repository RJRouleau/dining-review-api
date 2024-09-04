package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUserName(String userName);
}
