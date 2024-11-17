package org.mastersdbis.mtsd.Entities.Review;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Rating {
    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer professionalism;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer promptitude;

    @Min(value = 1)
    @Max(value = 5)
    private Integer quality;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer communication;

    @Min(value = 1)
    @Max(value = 5)
    private Integer price;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer overallSatisfaction;

    @Override
    public String toString() {
        return "Rating{" +
                "professionalism=" + professionalism +
                ", promptitude=" + promptitude +
                ", quality=" + (quality != null ? quality : "N/A") +
                ", communication=" + communication +
                ", price=" + (price != null ? price : "N/A") +
                ", overallSatisfaction=" + overallSatisfaction +
                '}';
    }
}