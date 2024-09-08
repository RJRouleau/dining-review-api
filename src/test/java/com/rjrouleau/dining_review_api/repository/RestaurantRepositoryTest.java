package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.Restaurant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RestaurantRepositoryTest {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantRepositoryTest(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    @Test
    public void RestaurantRepository_SaveRestaurant_ReturnSavedRestaurant(){
        Restaurant newRestaurant = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);

        Assertions.assertThat(savedRestaurant).isNotNull();
        Assertions.assertThat(savedRestaurant.getId()).isGreaterThan(0);
        Assertions.assertThat(savedRestaurant.getName()).isEqualTo(newRestaurant.getName());
    }

    @Test
    public void RestaurantRepository_FindById_ReturnOptionalRestaurant(){
        Restaurant newRestaurant = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);

        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(savedRestaurant.getId());

        Assertions.assertThat(restaurantOptional.isPresent()).isTrue();
        Assertions.assertThat(restaurantOptional.get().getId()).isEqualTo(savedRestaurant.getId());
    }

    @Test
    public void RestaurantRepository_FindByNameAndZipcode_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("Portland")
                .state("Oregon")
                .zipcode("00003")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);

        List<Restaurant> restaurantList1 = restaurantRepository.findByNameAndZipcode(
                savedRestaurant1.getName(),
                savedRestaurant1.getZipcode()
        );

        List<Restaurant> restaurantList2 = restaurantRepository.findByNameAndZipcode(
                savedRestaurant2.getName(),
                savedRestaurant2.getZipcode()
        );

        Assertions.assertThat(restaurantList1).isNotNull();
        Assertions.assertThat(restaurantList2).isNotNull();

        // restaurant1 and restaurant2 have the same name but different zipcodes.
        Assertions.assertThat(restaurantList1.contains(savedRestaurant1)).isTrue();
        Assertions.assertThat(restaurantList1.contains(savedRestaurant2)).isFalse();
        Assertions.assertThat(restaurantList2.contains(savedRestaurant2)).isTrue();
        Assertions.assertThat(restaurantList2.contains(savedRestaurant1)).isFalse();
    }

    @Test
    public void RestaurantRepository_FindByZipcode_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("Portland")
                .state("Oregon")
                .zipcode("00003")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);

        List<Restaurant> restaurantList = restaurantRepository.findByZipcode(savedRestaurant1.getZipcode());

        Assertions.assertThat(restaurantList).isNotNull();
        Assertions.assertThat(restaurantList.contains(savedRestaurant1)).isTrue();
        Assertions.assertThat(restaurantList.size()).isEqualTo(1);
    }

    @Test
    public void RestaurantRepository_FindByCity_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant2")
                .city("Portland")
                .state("Oregon")
                .zipcode("00003")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);

        List<Restaurant> restaurantList = restaurantRepository.findByCity(savedRestaurant1.getCity());

        Assertions.assertThat(restaurantList).isNotNull();
        Assertions.assertThat(restaurantList.contains(savedRestaurant1));
        Assertions.assertThat(restaurantList.size()).isEqualTo(1);
    }

    @Test
    public void RestaurantRepository_FindByState_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant2")
                .city("Portland")
                .state("Oregon")
                .zipcode("00003")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);

        List<Restaurant> restaurantList = restaurantRepository.findByState(savedRestaurant1.getState());

        Assertions.assertThat(restaurantList).isNotNull();
        Assertions.assertThat(restaurantList.contains(savedRestaurant1));
        Assertions.assertThat(restaurantList.size()).isEqualTo(1);
    }

    @Test
    public void
    RestaurantRepository_FindByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant3 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(null)
                .eggScore(null)
                .dairyScore(null)
                .name("testRestaurant3")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);
        Restaurant savedRestaurant3 = restaurantRepository.save(newRestaurant3);

        List<Restaurant> greaterThan2RestaurantList = restaurantRepository
                .findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(
                        savedRestaurant1.getZipcode(),
                        2.0f
                );
        List<Restaurant> greaterThan0RestaurantList = restaurantRepository
                .findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(
                        savedRestaurant1.getZipcode(),
                        0.0f
                );

        Assertions.assertThat(greaterThan2RestaurantList).isNotNull();
        Assertions.assertThat(greaterThan0RestaurantList).isNotNull();

        Assertions.assertThat(greaterThan2RestaurantList.contains(newRestaurant1)).isTrue();
        Assertions.assertThat(greaterThan2RestaurantList.size()).isEqualTo(1);

        Assertions.assertThat(greaterThan0RestaurantList.contains(newRestaurant1)).isTrue();
        Assertions.assertThat(greaterThan0RestaurantList.contains(newRestaurant2)).isTrue();
        Assertions.assertThat(greaterThan0RestaurantList.size()).isEqualTo(2);
        Assertions.assertThat(greaterThan0RestaurantList.get(0)).isEqualTo(newRestaurant1);
    }

    @Test
    public void
    RestaurantRepository_FindByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant3 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(null)
                .eggScore(null)
                .dairyScore(null)
                .name("testRestaurant3")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);
        Restaurant savedRestaurant3 = restaurantRepository.save(newRestaurant3);

        List<Restaurant> greaterThan2RestaurantList = restaurantRepository
                .findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(
                        savedRestaurant1.getZipcode(),
                        2.0f
                );
        List<Restaurant> greaterThan0RestaurantList = restaurantRepository
                .findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(
                        savedRestaurant1.getZipcode(),
                        0.0f
                );

        Assertions.assertThat(greaterThan2RestaurantList).isNotNull();
        Assertions.assertThat(greaterThan0RestaurantList).isNotNull();

        Assertions.assertThat(greaterThan2RestaurantList.contains(newRestaurant1)).isTrue();
        Assertions.assertThat(greaterThan2RestaurantList.size()).isEqualTo(1);

        Assertions.assertThat(greaterThan0RestaurantList.contains(newRestaurant1)).isTrue();
        Assertions.assertThat(greaterThan0RestaurantList.contains(newRestaurant2)).isTrue();
        Assertions.assertThat(greaterThan0RestaurantList.size()).isEqualTo(2);
        Assertions.assertThat(greaterThan0RestaurantList.get(0)).isEqualTo(newRestaurant1);
    }

    @Test
    public void
    RestaurantRepository_FindByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc_ReturnRestaurantList(){
        Restaurant newRestaurant1 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant1")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant2 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant newRestaurant3 = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(null)
                .eggScore(null)
                .dairyScore(null)
                .name("testRestaurant3")
                .city("San Francisco")
                .state("California")
                .zipcode("00002")
                .build();

        Restaurant savedRestaurant1 = restaurantRepository.save(newRestaurant1);
        Restaurant savedRestaurant2 = restaurantRepository.save(newRestaurant2);
        Restaurant savedRestaurant3 = restaurantRepository.save(newRestaurant3);

        List<Restaurant> greaterThan2RestaurantList = restaurantRepository
                .findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(
                        savedRestaurant1.getZipcode(),
                        2.0f
                );
        List<Restaurant> greaterThan0RestaurantList = restaurantRepository
                .findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(
                        savedRestaurant1.getZipcode(),
                        0.0f
                );

        Assertions.assertThat(greaterThan2RestaurantList).isNotNull();
        Assertions.assertThat(greaterThan0RestaurantList).isNotNull();

        Assertions.assertThat(greaterThan2RestaurantList.contains(newRestaurant1)).isTrue();
        Assertions.assertThat(greaterThan2RestaurantList.size()).isEqualTo(1);

        Assertions.assertThat(greaterThan0RestaurantList.contains(newRestaurant1)).isTrue();
        Assertions.assertThat(greaterThan0RestaurantList.contains(newRestaurant2)).isTrue();
        Assertions.assertThat(greaterThan0RestaurantList.size()).isEqualTo(2);
        Assertions.assertThat(greaterThan0RestaurantList.get(0)).isEqualTo(newRestaurant1);
    }
}
