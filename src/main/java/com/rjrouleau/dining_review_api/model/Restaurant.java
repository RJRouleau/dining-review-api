package com.rjrouleau.dining_review_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

@Entity
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

    public Restaurant(){

    }

    public Restaurant(Restaurant restaurant){
        this.userScores = restaurant.getUserScores();
        this.overallScore = restaurant.getOverallScore();
        this.peanutScore = restaurant.getPeanutScore();
        this.eggScore = restaurant.getEggScore();
        this.dairyScore = restaurant.getDairyScore();
        this.name = restaurant.getName();
        this.city = restaurant.getCity();
        this.state = restaurant.getState();
    }

    public List<Integer[]> getUserScores(){
        return this.userScores;
    }

    public void setUserScores(List<Integer[]> userScores){
        this.userScores = userScores;
    }

    public Float getOverallScore(){
        return this.overallScore;
    }

    public void setOverallScore(Float overallScore){
        this.overallScore = overallScore;
    }

    public Float getPeanutScore(){
        return this.peanutScore;
    }

    public void setPeanutScore(Float peanutScore){
        this.peanutScore = peanutScore;
    }

    public Float getEggScore(){
        return this.eggScore;
    }

    public void setEggScore(Float eggScore){
        this.eggScore = eggScore;
    }

    public Float getDairyScore(){
        return this.dairyScore;
    }

    public void setDairyScore(Float dairyScore){
        this.dairyScore = dairyScore;
    }
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getCity(){
        return this.city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getState(){
        return this.state;
    }

    public void setState(String state){
        this.state = state;
    }

}
