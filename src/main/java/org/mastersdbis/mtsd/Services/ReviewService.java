package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.ProviderRepository;
import org.mastersdbis.mtsd.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProviderRepository providerRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService, ProviderRepository providerRepository) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.providerRepository = providerRepository;
    }

    public void saveReview(Review review) {
        try {
            reviewRepository.save(review);
            userService.updateUserRating(review.getUser());
        } catch (DataAccessException e) {
            System.out.println("Eroare la salvarea review-ului si actualizarea ratingului utilizatorului: " + e.getMessage());
        }
    }

    public void deleteReview(Review review) {
        try {
            reviewRepository.delete(review);
            userService.updateUserRating(review.getUser());
        } catch (DataAccessException e) {
            System.out.println("Eroare la stergerea review-ului si la actualizarea ratingului utilizatorului: " + e.getMessage());
        }
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

    public double calculateAverageRatingForUser(User user) {
        List<Review> reviews = reviewRepository.findByUser(user);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        boolean isProvider = providerRepository.findByUser(user) != null;

        double sum = reviews.stream()
                .mapToDouble(review -> calculateAverageRatingForReview(review, isProvider))
                .sum();

        return sum / reviews.size();
    }

    private double calculateAverageRatingForReview(Review review, boolean isProvider) {
        double professionalism = (review.getRating().getProfessionalism() != null) ? review.getRating().getProfessionalism() : 0.0;
        double promptitude = (review.getRating().getPromptitude() != null) ? review.getRating().getPromptitude() : 0.0;
        double communication = (review.getRating().getCommunication() != null) ? review.getRating().getCommunication() : 0.0;
        double overallSatisfaction = (review.getRating().getOverallSatisfaction() != null) ? review.getRating().getOverallSatisfaction() : 0.0;

        if (isProvider) {
            double quality = (review.getRating().getQuality() != null) ? review.getRating().getQuality() : 0.0;
            double price = (review.getRating().getPrice() != null) ? review.getRating().getPrice() : 0.0;
            return (quality + price + professionalism + promptitude + communication + overallSatisfaction) / 6;
        } else {
            return (professionalism + promptitude + communication + overallSatisfaction) / 4;
        }
    }
}
