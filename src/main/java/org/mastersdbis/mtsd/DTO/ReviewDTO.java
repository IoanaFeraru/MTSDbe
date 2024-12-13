package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Review.Rating;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id; // Unique identifier for the review

    @NotNull(message = "User that left the review ID cannot be null.")
    private Integer userThatLeftTheReviewId; // ID of user leaving the review

    @NotNull(message = "User that is reviewed ID cannot be null.")
    private Integer userReviewedId; // ID of the user being reviewed

    @NotNull(message = "Service ID cannot be null.")
    private Integer serviceId; // ID of the associated service

    private Rating rating; // Embedded object for quality and price ratings

    @Size(max = 500, message = "Content must not exceed 500 characters.")
    private String content; // Review content

    @NotNull(message = "Review type cannot be null.")
    private ReviewType reviewType; // Enum representing the type of review
}