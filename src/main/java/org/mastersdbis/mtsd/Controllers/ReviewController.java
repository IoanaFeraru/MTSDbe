package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.RatingDTO;
import org.mastersdbis.mtsd.DTO.ReviewDTO;
import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.ReviewService;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//Posibil sa fie un mare 💩
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ServiceService serviceService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService, ServiceService serviceService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.serviceService = serviceService;
    }

    @PostMapping
    public ResponseEntity<String> saveReview(@RequestBody ReviewDTO reviewDTO) {
        User userThatLeftTheReview = userService.findById(reviewDTO.getUserThatLeftTheReviewId());
        User userReviewed = userService.findById(reviewDTO.getUserReviewedId());
        Service service = serviceService.findById(reviewDTO.getServiceId());

        RatingDTO ratingDTO = reviewDTO.getRating();
        if (ratingDTO == null) {
            return ResponseEntity.badRequest().body("Rating cannot be null.");
        }

        Review review = reviewDTO.toReview(userThatLeftTheReview, userReviewed, service, ratingDTO);
        reviewService.saveReview(review);
        return ResponseEntity.ok("Review saved successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> findById(@PathVariable int id) {
        Review review = reviewService.findById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ReviewDTO.fromReview(review));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ReviewDTO>> findByService(@PathVariable int serviceId) {
        Service service = new Service();
        service.setId(serviceId);
        List<ReviewDTO> reviews = reviewService.findByService(service)
                .stream()
                .map(ReviewDTO::fromReview)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/poster/{userId}")
    public ResponseEntity<List<ReviewDTO>> findByPoster(@PathVariable int userId) {
        User user = new User();
        user.setId(userId);
        List<ReviewDTO> reviews = reviewService.findByPoster(user)
                .stream()
                .map(ReviewDTO::fromReview)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviewed/{userId}")
    public ResponseEntity<List<ReviewDTO>> findByUserReviewed(@PathVariable int userId) {
        User user = new User();
        user.setId(userId);
        List<ReviewDTO> reviews = reviewService.findByUserReviewed(user)
                .stream()
                .map(ReviewDTO::fromReview)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }
}
