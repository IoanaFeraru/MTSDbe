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
    private Integer id;

    @NotNull(message = "User that left the review ID cannot be null.")
    private Integer userThatLeftTheReviewId;

    @NotNull(message = "User that is reviewed ID cannot be null.")
    private Integer userReviewedId;

    @NotNull(message = "Service ID cannot be null.")
    private Integer serviceId;

    private Rating rating;

    @Size(max = 500, message = "Content must not exceed 500 characters.")
    private String content;

    @NotNull(message = "Review type cannot be null.")
    private ReviewType reviewType;
}