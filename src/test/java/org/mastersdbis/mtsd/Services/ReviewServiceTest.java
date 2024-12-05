package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Review.Rating;
import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @Test
    void saveReview() {
        Review review = new Review();
        review.setReviewType(ReviewType.CUSTOMER_TO_PROVIDER);
        review.setService(serviceService.findAll().getLast());
        review.setUserThatLeftTheReview(userService.findByUsername("Stefan"));
        review.setUserReviewed(userService.findByUsername("Ioana"));
        review.setContent("test");

        Rating rating = new Rating();
        rating.setCommunication(5);
        rating.setPrice(5);
        rating.setQuality(5);
        rating.setPromptitude(5);
        rating.setProfessionalism(5);

        review.setRating(rating);

        reviewService.saveReview(review);

        Assertions.assertNotNull(reviewService.findById(review.getId()), "review should not be null");
        System.out.println(reviewService.findById(review.getId()) + "\n" + userService.findByUsername("Ioana"));
    }
}