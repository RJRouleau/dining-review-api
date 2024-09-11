package com.rjrouleau.dining_review_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjrouleau.dining_review_api.model.Restaurant;
import com.rjrouleau.dining_review_api.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Restaurant restaurant;

    @BeforeEach
    public void init(){
        restaurant = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();
    }

    @Test
    public void RestaurantController_CreateRestaurant_ReturnRestaurant() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(restaurant);

        given(restaurantRepository.findByNameAndZipcode(restaurant.getName(), restaurant.getZipcode()))
                .willReturn(List.of());
        given(restaurantRepository.save(Mockito.any(Restaurant.class))).willReturn(restaurant);

        mockMvc.perform(
                post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findByNameAndZipcode(restaurant.getName(), restaurant.getZipcode());
        Mockito.verify(restaurantRepository, Mockito.times(1)).save(restaurant);
    }


    @Test
    public void RestaurantController_CreateRestaurant_ReturnBadName() throws Exception {
        String restaurantJson = objectMapper.writeValueAsString(restaurant);
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);

        given(restaurantRepository.findByNameAndZipcode(restaurant.getName(), restaurant.getZipcode()))
                .willReturn(restaurantList);

        mockMvc.perform(
                        post("/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(restaurantJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant name must be unique for a given zipcode."));

        Mockito.verify(restaurantRepository, Mockito.times(1))
                .findByNameAndZipcode(restaurant.getName(), restaurant.getZipcode());
        Mockito.verify(restaurantRepository, Mockito.times(0)).save(restaurant);
    }

    @Test
    public void RestaurantController_GetRestaurants_ReturnRestaurantIterable() throws Exception {
        Restaurant restaurantDifferentName = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantDifferentName);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository.findAll()).willReturn(restaurantList);

        mockMvc.perform(
                get("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void RestaurantController_GetRestaurantById_ReturnRestaurant() throws Exception {
        Long restaurantId = Mockito.anyLong();
        String expectedJson = objectMapper.writeValueAsString(restaurant);

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        mockMvc.perform(
                get("/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findById(restaurantId);
    }

    @Test
    public void RestaurantController_GetRestaurantById_ReturnNotFound() throws Exception {
        Long restaurantId = Mockito.anyLong();

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        mockMvc.perform(
                        get("/restaurants/{id}", restaurantId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(restaurantRepository, Mockito.times(1)).findById(restaurantId);
    }

    @Test
    public void RestaurantController_GetRestaurantByZipcode_ReturnRestaurantList() throws Exception {
        Restaurant restaurantSameZipcode = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantSameZipcode);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository.findByZipcode(restaurant.getZipcode())).willReturn(restaurantList);

        mockMvc.perform(
                get("/restaurants/byzipcode/{zipcode}", restaurant.getZipcode())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findByZipcode(restaurant.getZipcode());
    }

    @Test
    public void RestaurantController_GetRestaurantByCity_ReturnRestaurantList() throws Exception {
        Restaurant restaurantSameCity = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantSameCity);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository.findByCity(restaurant.getCity())).willReturn(restaurantList);

        mockMvc.perform(
                        get("/restaurants/bycity/{city}", restaurant.getCity())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findByCity(restaurant.getCity());
    }

    @Test
    public void RestaurantController_GetRestaurantByState_ReturnRestaurantList() throws Exception {
        Restaurant restaurantSameState = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantSameState);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository.findByState(restaurant.getState())).willReturn(restaurantList);

        mockMvc.perform(
                        get("/restaurants/bystate/{state}", restaurant.getState())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findByState(restaurant.getState());
    }

    @Test
    public void RestaurantController_GetRestaurantByZipcodeAllergyDesc_ReturnPeanutRestaurantList() throws Exception {
        Restaurant restaurantLowerScore = Restaurant.builder()
                .overallScore(1.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantLowerScore);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository
                .findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(restaurant.getZipcode(), 0.f)
        ).willReturn(restaurantList);

        mockMvc.perform(
                get(
                        "/restaurants/search?zipcode={zipcode}&allergy={allergy}",
                        restaurant.getZipcode(),
                        "peanut"
                )
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1))
                .findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(restaurant.getZipcode(), 0.f);
    }

    @Test
    public void RestaurantController_GetRestaurantByZipcodeAllergyDesc_ReturnEggRestaurantList() throws Exception {
        Restaurant restaurantLowerScore = Restaurant.builder()
                .overallScore(1.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantLowerScore);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository
                .findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(restaurant.getZipcode(), 0.f)
        ).willReturn(restaurantList);

        mockMvc.perform(
                        get(
                                "/restaurants/search?zipcode={zipcode}&allergy={allergy}",
                                restaurant.getZipcode(),
                                "egg"
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1))
                .findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(restaurant.getZipcode(), 0.f);
    }

    @Test
    public void RestaurantController_GetRestaurantByZipcodeAllergyDesc_ReturnDairyRestaurantList() throws Exception {
        Restaurant restaurantLowerScore = Restaurant.builder()
                .overallScore(1.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        restaurantList.add(restaurantLowerScore);

        String expectedJson = objectMapper.writeValueAsString(restaurantList);

        given(restaurantRepository
                .findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(restaurant.getZipcode(), 0.f)
        ).willReturn(restaurantList);

        mockMvc.perform(
                        get(
                                "/restaurants/search?zipcode={zipcode}&allergy={allergy}",
                                restaurant.getZipcode(),
                                "dairy"
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1))
                .findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(restaurant.getZipcode(), 0.f);
    }

    @Test
    public void RestaurantController_GetRestaurantByZipcodeAllergyDesc_ReturnInvalidZipcode() throws Exception {
        Restaurant restaurantInvalidZipcode = Restaurant.builder()
                .overallScore(1.0f)
                .peanutScore(1.0f)
                .eggScore(1.0f)
                .dairyScore(1.0f)
                .name("testRestaurant2")
                .city("Chicago")
                .state("Illinois")
                .zipcode("03f5")
                .build();

        mockMvc.perform(
                        get(
                                "/restaurants/search?zipcode={zipcode}&allergy={allergy}",
                                restaurantInvalidZipcode.getZipcode(),
                                "dairy"
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid zipcode. Zipcode must be 5 digits."));

        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(Mockito.anyString(), Mockito.anyFloat());
        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(Mockito.anyString(), Mockito.anyFloat());
        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(Mockito.anyString(), Mockito.anyFloat());
        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcode(Mockito.anyString());
    }

    @Test
    public void RestaurantController_GetRestaurantByZipcodeAllergyDesc_ReturnInvalidAllergy() throws Exception {
        String invalidAllergy = "raspberry";

        mockMvc.perform(
                        get(
                                "/restaurants/search?zipcode={zipcode}&allergy={allergy}",
                                restaurant.getZipcode(),
                                invalidAllergy
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid allergy. Allergy must be peanut, egg, or dairy."));

        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcodeAndPeanutScoreGreaterThanOrderByPeanutScoreDesc(Mockito.anyString(), Mockito.anyFloat());
        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcodeAndEggScoreGreaterThanOrderByEggScoreDesc(Mockito.anyString(), Mockito.anyFloat());
        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcodeAndDairyScoreGreaterThanOrderByDairyScoreDesc(Mockito.anyString(), Mockito.anyFloat());
        Mockito.verify(restaurantRepository, Mockito.times(0))
                .findByZipcode(Mockito.anyString());
    }

    @Test
    public void RestaurantController_UpdateRestaurant_ReturnRestaurant() throws Exception {
        Restaurant updatedRestaurant = Restaurant.builder()
                .overallScore(5.0f)
                .peanutScore(5.0f)
                .eggScore(5.0f)
                .dairyScore(5.0f)
                .name("testRestaurant")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();
        String expectedJson = objectMapper.writeValueAsString(updatedRestaurant);

        given(restaurantRepository.findById(Mockito.anyLong())).willReturn(Optional.of(restaurant));
        given(restaurantRepository.save(Mockito.any(Restaurant.class))).willReturn(updatedRestaurant);

        mockMvc.perform(
                put("/restaurants/{id}", Mockito.anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(restaurantRepository, Mockito.times(1)).save(Mockito.any(Restaurant.class));
    }

    @Test
    public void RestaurantController_UpdateRestaurant_ReturnNotFound() throws Exception {
        String invalidRestaurant = objectMapper.writeValueAsString(restaurant);

        given(restaurantRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        mockMvc.perform(
                        put("/restaurants/{id}", Mockito.anyLong())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRestaurant)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(restaurantRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(restaurantRepository, Mockito.times(0)).save(Mockito.any(Restaurant.class));
    }

    @Test
    public void RestaurantController_DeleteRestaurant_ReturnRestaurant() throws Exception {
        Long restaurantId = Mockito.anyLong();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        String expectedJson = objectMapper.writeValueAsString(restaurant);

        mockMvc.perform(
                delete("/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andExpect(content().json(expectedJson));

        Mockito.verify(restaurantRepository, Mockito.times(1)).delete(restaurant);
    }

    @Test
    public void RestaurantController_DeleteRestaurant_ReturnNotFound() throws Exception {
        Long restaurantId = Mockito.anyLong();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        mockMvc.perform(
                        delete("/restaurants/{id}", restaurantId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(restaurantRepository, Mockito.times(0)).delete(restaurant);
    }
}
