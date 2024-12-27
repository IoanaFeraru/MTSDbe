package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Review.Rating;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer professionalism;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer promptitude;

    @Min(1)
    @Max(5)
    private Integer quality;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer communication;

    @Min(1)
    @Max(5)
    private Integer price;

    @NotNull
    @Min(1)
    @Max(5)
    private Double overallSatisfaction;

    public Rating toRating() {
        Rating rating = new Rating();
        rating.setProfessionalism(professionalism);
        rating.setPromptitude(promptitude);
        rating.setQuality(quality);
        rating.setCommunication(communication);
        rating.setPrice(price);
        rating.setOverallSatisfaction(overallSatisfaction);
        return rating;
    }

    public static RatingDTO fromRating(Rating rating) {
        if (rating == null) {
            return null;
        }
        return new RatingDTO(
                rating.getProfessionalism(),
                rating.getPromptitude(),
                rating.getQuality(),
                rating.getCommunication(),
                rating.getPrice(),
                rating.getOverallSatisfaction()
        );
    }
}
