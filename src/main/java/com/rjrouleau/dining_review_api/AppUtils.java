package com.rjrouleau.dining_review_api;

import com.rjrouleau.dining_review_api.model.Restaurant;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AppUtils {

    // Updates the value of an object's field with a Consumer if the value provided by the Supplier is not null.
    public static <T> void setIfNotNull(Supplier<T> getter, Consumer<T> setter) {
        T value = getter.get();
        if (value != null) {
            setter.accept(value);
        }
    }

    // Calculates the overall score as the average of available allergy scores.
    public static Float calculateOverallScore(Restaurant restaurant) {
        Float peanutScore = restaurant.getPeanutScore() != null ? restaurant.getPeanutScore() : 0.f;
        Float eggScore = restaurant.getEggScore() != null ? restaurant.getEggScore() : 0.f;
        Float dairyScore = restaurant.getDairyScore() != null ? restaurant.getDairyScore() : 0.f;

        Float overallScore = (peanutScore + eggScore + dairyScore) / 3.f;
        return overallScore;
    }
}
