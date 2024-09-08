package com.rjrouleau.dining_review_api.repository;

import com.rjrouleau.dining_review_api.model.Review;
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
public class ReviewRepositoryTest {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepository_SaveReview_ReturnSavedReview(){
        Review newReview = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review for a restaurant.")
                .build();

        Review savedReview = reviewRepository.save(newReview);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
        Assertions.assertThat(savedReview.getUserName()).isEqualTo(newReview.getUserName());
        Assertions.assertThat(savedReview.getRestaurantName()).isEqualTo(newReview.getRestaurantName());
        Assertions.assertThat(savedReview.getPeanutScore()).isEqualTo(newReview.getPeanutScore());
        Assertions.assertThat(savedReview.getEggScore()).isEqualTo(newReview.getEggScore());
        Assertions.assertThat(savedReview.getDairyScore()).isEqualTo(newReview.getDairyScore());
        Assertions.assertThat(savedReview.getCommentary()).isEqualTo(newReview.getCommentary());
    }

    @Test
    public void ReviewRepository_FindById_ReturnOptionalReview(){
        Review newReview = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review for a restaurant.")
                .build();

        Review savedReview = reviewRepository.save(newReview);

        Optional<Review> reviewOptional = reviewRepository.findById(savedReview.getId());

        Assertions.assertThat(reviewOptional.isPresent()).isTrue();
        Assertions.assertThat(reviewOptional.get().getId()).isEqualTo(savedReview.getId());
    }

    @Test
    public void ReviewRepository_FindByUserName_ReturnReviewList(){
        Review newReview1 = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant1")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review for restaurant1.")
                .build();

        Review newReview2 = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant2")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review for restaurant2.")
                .build();

        Review savedReview1 = reviewRepository.save(newReview1);
        Review savedReview2 = reviewRepository.save(newReview2);

        List<Review> reviewList = reviewRepository.findByUserName(savedReview1.getUserName());

        Assertions.assertThat(reviewList).isNotNull();
        Assertions.assertThat(reviewList.size()).isEqualTo(2);
        Assertions.assertThat(reviewList.contains(savedReview1)).isTrue();
        Assertions.assertThat(reviewList.contains(savedReview2)).isTrue();
    }

    @Test
    public void ReviewRepository_FindByStatus_ReturnReviewList(){
        Review newReview1 = Review.builder()
                .userName("testUser1")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review from testUser1.")
                .status(Review.Status.PENDING)
                .build();

        Review newReview2 = Review.builder()
                .userName("testUser2")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review from testUser2.")
                .status(Review.Status.PENDING)
                .build();

        Review newReview3 = Review.builder()
                .userName("testUser3")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review from testUser3.")
                .status(Review.Status.REJECTED)
                .build();

        Review newReview4 = Review.builder()
                .userName("testUser4")
                .restaurantName("testRestaurant")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review from testUser3.")
                .status(Review.Status.ACCEPTED)
                .build();

        Review savedReview1 = reviewRepository.save(newReview1);
        Review savedReview2 = reviewRepository.save(newReview2);
        Review savedReview3 = reviewRepository.save(newReview3);
        Review savedReview4 = reviewRepository.save(newReview4);

        List<Review> pendingReviewList = reviewRepository.findByStatus(Review.Status.PENDING);
        List<Review> acceptedReviewList = reviewRepository.findByStatus(Review.Status.ACCEPTED);
        List<Review> rejectedReviewList = reviewRepository.findByStatus(Review.Status.REJECTED);

        Assertions.assertThat(pendingReviewList).isNotNull();
        Assertions.assertThat(acceptedReviewList).isNotNull();
        Assertions.assertThat(rejectedReviewList).isNotNull();

        Assertions.assertThat(pendingReviewList.size()).isEqualTo(2);
        Assertions.assertThat(acceptedReviewList.size()).isEqualTo(1);

        Assertions.assertThat(pendingReviewList.get(0).getStatus()).isEqualTo(Review.Status.PENDING);
        Assertions.assertThat(acceptedReviewList.get(0).getStatus()).isEqualTo(Review.Status.ACCEPTED);
        Assertions.assertThat(rejectedReviewList.get(0).getStatus()).isEqualTo(Review.Status.REJECTED);
    }

    @Test
    public void ReviewRepository_FindByRestaurantNameAndStatus_ReturnReviewList(){
        Review newReview1 = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant1")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("A review for restaurant1.")
                .status(Review.Status.ACCEPTED)
                .build();

        Review newReview2 = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant1")
                .peanutScore(3)
                .eggScore(2)
                .dairyScore(null)
                .commentary("Another review for restaurant1.")
                .status(Review.Status.ACCEPTED)
                .build();

        Review newReview3 = Review.builder()
                .userName("testUser")
                .restaurantName("testRestaurant1")
                .peanutScore(null)
                .eggScore(null)
                .dairyScore(null)
                .commentary(null)
                .status(Review.Status.REJECTED)
                .build();

        Review savedReview1 = reviewRepository.save(newReview1);
        Review savedReview2 = reviewRepository.save(newReview2);
        Review savedReview3 = reviewRepository.save(newReview3);

        List<Review> acceptedReviewList = reviewRepository.findByRestaurantNameAndStatus(
                savedReview1.getRestaurantName(),
                Review.Status.ACCEPTED
        );

        List<Review> rejectedReviewList = reviewRepository.findByRestaurantNameAndStatus(
                savedReview3.getRestaurantName(),
                Review.Status.REJECTED
        );

        Assertions.assertThat(acceptedReviewList).isNotNull();
        Assertions.assertThat(acceptedReviewList.size()).isEqualTo(2);
        Assertions.assertThat(acceptedReviewList.contains(savedReview1)).isTrue();
        Assertions.assertThat(acceptedReviewList.contains(savedReview2)).isTrue();
        Assertions.assertThat(acceptedReviewList.contains(savedReview3)).isFalse();

        Assertions.assertThat(rejectedReviewList).isNotNull();
        Assertions.assertThat(rejectedReviewList.size()).isEqualTo(1);
        Assertions.assertThat(rejectedReviewList.contains(savedReview3)).isTrue();
    }
}
