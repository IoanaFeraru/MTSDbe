package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByService(Service service);

    List<Review> findByUser(User user);

    List<Review> findByUserAndReviewType(User user, ReviewType type);

    @Query("SELECT COALESCE(SUM(r.rating.overallSatisfaction), 0) FROM Review r WHERE r.user = :user")
    Double sumOfReviewsByUser(@Param("user") User user);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user")
    int countReviewsByUser(@Param("user") User user);
}
