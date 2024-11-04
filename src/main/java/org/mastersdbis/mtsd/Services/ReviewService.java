package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public void updateReview(Review review) {
        reviewRepository.save(review);
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public Review findById(int id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findByService(Service service) {
        return reviewRepository.findByService(service);
    }

    public List<Review> findByPoster(User user) {
        return reviewRepository.findByUser(user);
    }

    public List<Review> findByUserAndType(User user, ReviewType reviewType) {
        return reviewRepository.findByUserAndReviewType(user, reviewType);
    }

    //TODO implementare exceptii
}
