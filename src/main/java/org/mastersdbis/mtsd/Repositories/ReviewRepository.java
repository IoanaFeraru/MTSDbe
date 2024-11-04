package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Review.Review;
import org.mastersdbis.mtsd.Entities.Review.ReviewType;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByService(Service service);

    List<Review> findByUser(User user);

    List<Review> findByUserAndReviewType(User user, ReviewType type);
}
