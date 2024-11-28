package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Review.Rating;
import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.ProviderRepository;
import org.mastersdbis.mtsd.Repositories.ReviewRepository;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProviderRepository providerRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    public void saveReview(Review review) {
        try {
            User userToUpdate = userRepository.findById(review.getService().getProvider().getId()).orElseThrow(() -> new IllegalArgumentException("User cannot be null"));
            boolean isProvider = providerRepository.findByUser(userToUpdate) != null;

            double averageRating = calculateAverageRatingForReview(review, isProvider);
            Rating rating = review.getRating();
            rating.setOverallSatisfaction(averageRating);
            review.setRating(rating);
            reviewRepository.save(review);

            userToUpdate.setRating(calculateAverageRatingForUser(userToUpdate, averageRating));
            userRepository.save(userToUpdate);
        } catch (DataAccessException e) {
            System.out.println("Eroare la salvarea review-ului si actualizarea ratingului utilizatorului: " + e.getMessage());
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

    //ToDO ioana rezolva asta ms
    public double calculateAverageRatingForUser(User user, Double ratingAdaugat) {
        Double sum = reviewRepository.sumOfReviewsByUser(user);
        if (sum == null) {
            sum = 0.0;
        }

        int count = reviewRepository.countReviewsByUser(user);

        if (count == 0) {
            return ratingAdaugat;
        }

        System.out.println(sum + "" + count);
        return (sum + ratingAdaugat) / (count + 1);
    }



    private double calculateAverageRatingForReview(Review review, boolean isProvider) {
        double professionalism = (review.getRating().getProfessionalism() != null) ? review.getRating().getProfessionalism() : 0.0;
        double promptitude = (review.getRating().getPromptitude() != null) ? review.getRating().getPromptitude() : 0.0;
        double communication = (review.getRating().getCommunication() != null) ? review.getRating().getCommunication() : 0.0;
        double overallSatisfaction;

        if (isProvider) {
            double quality = (review.getRating().getQuality() != null) ? review.getRating().getQuality() : 0.0;
            double price = (review.getRating().getPrice() != null) ? review.getRating().getPrice() : 0.0;
            overallSatisfaction = (quality + price + professionalism + promptitude + communication) / 5;
        } else {
            overallSatisfaction = (professionalism + promptitude + communication) / 3;
        }
        return overallSatisfaction;
    }
}
