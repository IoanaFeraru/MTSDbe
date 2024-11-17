package org.mastersdbis.mtsd.Repositories;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByService(Service service);

    List<Booking> findByUser(User user);

    List<Booking> findByDueDateAndDueTime(LocalDate dueDate, LocalDateTime dueTime);

    List<Booking> findByDueDateBetween(LocalDate dateStart, LocalDate dateEnd);

    List<Booking> findByUserAndBookingState(User user, BookingState state);

    List<Booking> findByUserAndBookingStateAndDueDate(@NotNull(message = "User cannot be null.") User user, @NotNull(message = "Booking state cannot be null.") BookingState bookingState, @Future(message = "Due date must be in the future.") LocalDate dueDate);

    @Query("SELECT b FROM Booking b JOIN b.service s WHERE s.provider = :provider")
    List<Booking> findByServiceProvider(User provider);

    @Query("SELECT b FROM Booking b JOIN b.service s WHERE s.provider = :provider AND b.bookingState = :status")
    List<Booking> findByServiceProviderAndBookingState(User provider, BookingState status);
}

