package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Test
    public void UserRepository_SaveUser_ReturnSavedUser(){

        User user = User.builder()
                .userName("testUser")
                .city("Fremont")
                .state("California")
                .zipcode("00001")
                .peanutAllergy(true)
                .eggAllergy(false)
                .dairyAllergy(false)
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
        Assertions.assertThat(savedUser.getUserName()).isEqualTo("testUser");
        Assertions.assertThat(savedUser.getCity()).isEqualTo("Fremont");
        Assertions.assertThat(savedUser.getState()).isEqualTo("California");
        Assertions.assertThat(savedUser.getZipcode()).isEqualTo("00001");
        Assertions.assertThat(savedUser.getPeanutAllergy()).isEqualTo(true);
        Assertions.assertThat(savedUser.getEggAllergy()).isEqualTo(false);
        Assertions.assertThat(savedUser.getDairyAllergy()).isEqualTo(false);
    }

    @Test
    public void UserRepository_FindByUserName_ReturnOptionalUser(){
        User newUser = User.builder()
                .userName("testUser")
                .city("Fremont")
                .state("California")
                .zipcode("00001")
                .peanutAllergy(true)
                .eggAllergy(false)
                .dairyAllergy(false)
                .build();

        User savedUser = userRepository.save(newUser);

        Optional<User> userOptional = userRepository.findByUserName(newUser.getUserName());
        Optional<User> emptyOptional = userRepository.findByUserName("");

        Assertions.assertThat(userOptional.isPresent()).isTrue();
        Assertions.assertThat(emptyOptional.isEmpty()).isTrue();

        User user = userOptional.get();

        Assertions.assertThat(user.getUserName()).isEqualTo(newUser.getUserName());
    }
}
