package com.rjrouleau.dining_review_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjrouleau.dining_review_api.model.Restaurant;
import com.rjrouleau.dining_review_api.model.Review;
import com.rjrouleau.dining_review_api.repository.RestaurantRepository;
import com.rjrouleau.dining_review_api.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Review review;

    @BeforeEach
    public void init(){
        review = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(3)
                .dairyScore(3)
                .commentary("This is a test review.")
                .status(Review.Status.PENDING)
                .build();
    }

    @Test
    public void ReviewController_CreateReview_ReturnReview() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(review);

        given(reviewRepository.save(Mockito.any(Review.class))).willReturn(review);

        mockMvc.perform(
                        post("/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(expectedJson)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));

        Mockito.verify(reviewRepository, Mockito.times(1)).save(review);
    }

    @Test
    public void ReviewController_GetReviewById_ReturnReview() throws Exception {
        Long reviewId = Mockito.anyLong();
        String expectedJson = objectMapper.writeValueAsString(review);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        mockMvc.perform(
                get("/reviews/{id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    public void ReviewController_GetReviewById_ReturnNotFound() throws Exception {
        Long reviewId = Mockito.anyLong();

        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        mockMvc.perform(
                        get("/reviews/{id}", reviewId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    public void ReviewController_GetApprovedReviewsByRestaurantName_ReturnReviewList() throws Exception {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);
        String expectedJson = objectMapper.writeValueAsString(reviewList);

        given(reviewRepository.findByRestaurantNameAndStatus(review.getRestaurantName(), Review.Status.ACCEPTED))
                .willReturn(reviewList);

        mockMvc.perform(
                get("/reviews/restaurant/{restaurantName}", review.getRestaurantName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(reviewRepository, Mockito.times(1))
                .findByRestaurantNameAndStatus(review.getRestaurantName(), Review.Status.ACCEPTED);
    }

    @Test
    public void ReviewController_GetApprovedReviewsByRestaurantName_ReturnNotFound() throws Exception {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);

        given(reviewRepository.findByRestaurantNameAndStatus(review.getRestaurantName(), Review.Status.ACCEPTED))
                .willReturn(List.of());

        mockMvc.perform(
                        get("/reviews/restaurant/{restaurantName}", review.getRestaurantName())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(reviewRepository, Mockito.times(1))
                .findByRestaurantNameAndStatus(review.getRestaurantName(), Review.Status.ACCEPTED);
    }

    @Test
    public void ReviewController_GetReviewsByUserName_ReturnReviewList() throws Exception {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);
        String expectedJson = objectMapper.writeValueAsString(reviewList);

        given(reviewRepository.findByUserName(review.getUserName())).willReturn(reviewList);

        mockMvc.perform(
                        get("/reviews/user/{userName}", review.getUserName())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(reviewRepository, Mockito.times(1)).findByUserName(review.getUserName());
    }

    @Test
    public void ReviewController_GetReviewsByUserName_ReturnNotFound() throws Exception {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);

        given(reviewRepository.findByUserName(review.getUserName())).willReturn(List.of());

        mockMvc.perform(
                        get("/reviews/user/{userName}", review.getUserName())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(reviewRepository, Mockito.times(1)).findByUserName(review.getUserName());
    }

    @Test
    public void ReviewController_UpdateReview_ReturnReview() throws Exception {
        Review updatedReview = Review.builder()
                .userName("testUser2")
                .restaurantName("testRestaurant2")
                .peanutScore(3)
                .eggScore(3)
                .dairyScore(3)
                .commentary("This is a test review.")
                .build();

        String expectedJson = objectMapper.writeValueAsString(updatedReview);

        given(reviewRepository.findById(Mockito.anyLong())).willReturn(Optional.of(review));
        given(reviewRepository.save(Mockito.any(Review.class))).willReturn(updatedReview);

        mockMvc.perform(
                        put("/reviews/{id}", Mockito.anyLong())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(expectedJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
    }

    @Test
    public void ReviewController_UpdateReview_ReturnNotFound() throws Exception {
        String invalidReview = objectMapper.writeValueAsString(review);
        given(reviewRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        mockMvc.perform(
                        put("/reviews/{id}", Mockito.anyLong())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidReview)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.times(0)).save(Mockito.any(Review.class));
    }

    @Test
    public void ReviewController_GetReviewsPendingApproval_ReturnReviewList() throws Exception {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);
        String expectedJson = objectMapper.writeValueAsString(reviewList);

        given(reviewRepository.findByStatus(Review.Status.PENDING)).willReturn(reviewList);

        mockMvc.perform(
                get("/reviews/admin")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(reviewRepository, Mockito.times(1)).findByStatus(Review.Status.PENDING);
    }

    @Test
    public void ReviewController_GetReviewsPendingApproval_ReturnNotFound() throws Exception {

        given(reviewRepository.findByStatus(Review.Status.PENDING)).willReturn(List.of());

        mockMvc.perform(
                        get("/reviews/admin")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(reviewRepository, Mockito.times(1)).findByStatus(Review.Status.PENDING);
    }

    @Test
    public void ReviewController_UpdateReviewStatus_ReturnReview() throws Exception {
        String updatedStatus = "accepted";
        Review updatedReview = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(3)
                .dairyScore(3)
                .commentary("This is a test review.")
                .status(Review.Status.ACCEPTED)
                .build();

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(updatedReview);

        String expectedJson = objectMapper.writeValueAsString(updatedReview);

        Restaurant restaurant = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();


        given(reviewRepository.findById(Mockito.anyLong())).willReturn(Optional.of(review));
        given(reviewRepository.save(Mockito.any(Review.class))).willReturn(updatedReview);
        given(reviewRepository.findByRestaurantNameAndStatus(review.getRestaurantName(), Review.Status.ACCEPTED))
                .willReturn(reviewList);
        given(restaurantRepository.findById(Mockito.any())).willReturn(Optional.of(restaurant));

        mockMvc.perform(
                put("/reviews/admin/{id}", Mockito.anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedStatus)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));

    }

    @Test
    public void ReviewController_UpdateReviewStatus_ReturnNotFound() throws Exception {
        String updatedStatus = "accepted";

        given(reviewRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        mockMvc.perform(
                put("/reviews/admin/{id}", Mockito.anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedStatus)
        )
                .andExpect(status().isNotFound());

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.times(0)).save(Mockito.any(Review.class));
    }

    @Test
    public void ReviewController_UpdateReviewStatus_ReturnInternalServerError() throws Exception {
        String updatedStatus = "accepted";
        Review updatedReview = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(3)
                .dairyScore(3)
                .commentary("This is a test review.")
                .status(Review.Status.ACCEPTED)
                .build();

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(updatedReview);

        String expectedJson = objectMapper.writeValueAsString(updatedReview);

        Restaurant restaurant = Restaurant.builder()
                .overallScore(3.0f)
                .peanutScore(3.0f)
                .eggScore(3.0f)
                .dairyScore(3.0f)
                .name("testRestaurant")
                .city("Chicago")
                .state("Illinois")
                .zipcode("00005")
                .build();


        given(reviewRepository.findById(Mockito.anyLong())).willReturn(Optional.of(review));
        given(reviewRepository.save(Mockito.any(Review.class))).willReturn(updatedReview);
        given(reviewRepository.findByRestaurantNameAndStatus(review.getRestaurantName(), Review.Status.ACCEPTED))
                .willReturn(reviewList);
        // return Optional.empty() to mock an internal server error.
        given(restaurantRepository.findById(Mockito.any())).willReturn(Optional.empty());

        mockMvc.perform(
                        put("/reviews/admin/{id}", Mockito.anyLong())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedStatus)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while updating restaurant scores."));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
    }

    @Test
    public void ReviewController_DeleteReview_ReturnReview() throws Exception {
        Long reviewId = Mockito.anyLong();
        String expectedJson = objectMapper.writeValueAsString(review);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        mockMvc.perform(
                delete("/reviews/{id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andExpect(content().json(expectedJson));
        Mockito.verify(reviewRepository, Mockito.times(1)).delete(review);
    }

    @Test
    public void ReviewController_DeleteReview_ReturnNotFound() throws Exception {
        Long reviewId = Mockito.anyLong();

        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        mockMvc.perform(
                        delete("/reviews/{id}", reviewId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        Mockito.verify(reviewRepository, Mockito.times(0)).delete(review);
    }

}
