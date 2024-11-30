package org.mastersdbis.mtsd.Repositories;

import jakarta.validation.constraints.NotNull;
import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByService(Service service);

    List<Review> findByUserReviewed(@NotNull(message = "User cannot be null.") User userReviewed);

    List<Review> findByUserThatLeftTheReview(@NotNull(message = "User cannot be null.") User userThatLeftTheReview);

    @Query("SELECT COALESCE(SUM(r.rating.overallSatisfaction), 0) FROM Review r WHERE r.userReviewed = :user")
    Double sumOfReviewsByUserReviewed(@Param("user") User user);

    @Query("SELECT COALESCE(COUNT(r), 0) FROM Review r WHERE r.userReviewed = :user")
    int countReviewsByUserReviewed(@Param("user") User user);
}
