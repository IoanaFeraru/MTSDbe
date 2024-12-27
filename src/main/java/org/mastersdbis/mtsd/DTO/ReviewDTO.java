package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.Service.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;

    @NotNull(message = "User that left the review ID cannot be null.")
    private int userThatLeftTheReviewId;

    @NotNull(message = "User that is reviewed ID cannot be null.")
    private int userReviewedId;

    @NotNull(message = "Service ID cannot be null.")
    private Integer serviceId;

    private RatingDTO rating;

    @Size(max = 500, message = "Content must not exceed 500 characters.")
    private String content;

    @NotNull(message = "Review type cannot be null.")
    private ReviewType reviewType;

    public Review toReview(User userThatLeftTheReview, User userReviewed, Service service, RatingDTO ratingDTO) {
        Review review = new Review();
        review.setId(id);

        review.setUserThatLeftTheReview(userThatLeftTheReview);
        review.setUserReviewed(userReviewed);
        review.setService(service);
        review.setRating(ratingDTO != null ? ratingDTO.toRating() : null);
        review.setContent(content);
        review.setReviewType(reviewType);

        return review;
    }

    public static ReviewDTO fromReview(Review review) {
        if (review == null) {
            return null;
        }
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserThatLeftTheReviewId(review.getUserThatLeftTheReview().getId());
        dto.setUserReviewedId(review.getUserReviewed().getId());
        dto.setServiceId(review.getService().getId());
        dto.setRating(RatingDTO.fromRating(review.getRating()));
        dto.setContent(review.getContent());
        dto.setReviewType(review.getReviewType());
        return dto;
    }
}
