package com.rjrouleau.dining_review_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private String city;
    private String state;
    private String zipcode;

    private Boolean peanutAllergy;
    private Boolean eggAllergy;
    private Boolean dairyAllergy;
}
