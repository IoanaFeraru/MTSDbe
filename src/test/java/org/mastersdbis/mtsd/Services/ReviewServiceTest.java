package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Review.Rating;
import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
        review.setUser(userService.findByUsername("Stefan"));
        review.setContent("test3");

        Rating rating = new Rating();
        rating.setCommunication(2);
        rating.setPrice(1);
        rating.setQuality(4);
        rating.setPromptitude(3);
        rating.setProfessionalism(5);

        review.setRating(rating);

        reviewService.saveReview(review);

        Assertions.assertNotNull(reviewService.findById(review.getId()), "review should not be null");
        System.out.println(reviewService.findById(review.getId()) + "\n" + userService.findByUsername("Ioana"));
    }
}