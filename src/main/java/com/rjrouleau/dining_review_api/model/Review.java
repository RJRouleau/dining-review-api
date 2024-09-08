package com.rjrouleau.dining_review_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private String restaurantName;
    private Integer peanutScore;
    private Integer eggScore;
    private Integer dairyScore;
    private String commentary;

    public static enum Status {
        PENDING, ACCEPTED, REJECTED;
    }
    private Status status;
}
