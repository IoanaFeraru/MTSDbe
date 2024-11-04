package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByService(Service service);

    List<Booking> findByUser(User user);

    List<Booking> findByBookingDate(LocalDate bookingDate);

    List<Booking> findByBookingDateBetween(LocalDate dateStart, LocalDate dateEnd);

    @Query("SELECT b FROM Booking b JOIN b.service s WHERE s.provider = :provider")
    List<Booking> findByServiceProvider(User provider);

    @Query("SELECT b FROM Booking b JOIN b.service s WHERE s.provider = :provider AND b.bookingState = :status")
    List<Booking> findByServiceProviderAndBookingState(User provider, BookingState status);
}

