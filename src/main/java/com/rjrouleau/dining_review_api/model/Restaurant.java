package com.rjrouleau.dining_review_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue
    private Long id;

    private List<Integer[]> userScores;

    private Float overallScore;
    private Float peanutScore;
    private Float eggScore;
    private Float dairyScore;

    private String name;
    private String city;
    private String state;
    private String zipcode;

}
