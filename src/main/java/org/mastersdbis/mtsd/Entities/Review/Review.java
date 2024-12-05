package org.mastersdbis.mtsd.Entities.Review;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_gen")
    @SequenceGenerator(name = "review_id_gen", sequenceName = "review_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "userThatLeftTheReview cannot be null.")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_that_left_the_review", referencedColumnName = "id", nullable = false)
    private User userThatLeftTheReview;

    @NotNull(message = "userReviewed cannot be null.")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_Reviewed", referencedColumnName = "id", nullable = false)
    private User userReviewed;

    @NotNull(message = "Service cannot be null.")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Embedded
    private Rating rating;

    @Size(max = 500, message = "Content must not exceed 500 characters.")
    @Column(name = "content", length = 500)
    private String content;

    @NotNull(message = "Review type cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType;

    @Override
    public Integer getId() { return id; }

    @AssertTrue(message = "Quality and price must be null if the review is from a provider.")
    public boolean isValidRating() {
        if (this.reviewType == ReviewType.PROVIDER_TO_CLIENT) {
            return rating.getQuality() == null && rating.getPrice() == null;
        } else if (this.reviewType == ReviewType.CUSTOMER_TO_PROVIDER) {
            return rating.getQuality() != null && rating.getPrice() != null;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userThatReviewed=" + (userThatLeftTheReview != null ? userThatLeftTheReview.getId() : "null") +
                ", userReviewed=" + (userReviewed != null ? userReviewed.getId() : "null") +
                ", serviceId=" + (service != null ? service.getId() : "null") +
                ", rating=" + (rating != null ? rating.toString() : "null") +
                ", content='" + content + '\'' +
                ", reviewType=" + reviewType +
                '}';
    }
}
