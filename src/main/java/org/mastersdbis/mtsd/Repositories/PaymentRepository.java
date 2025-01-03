package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findByBooking(Booking booking);

    @Query("SELECT p FROM Payment p WHERE p.booking.user = :user")
    List<Payment> findByUser(@Param("user") User user);

    List<Payment> findByPaymentState(PaymentState paymentState);

    @Query("SELECT p FROM Payment p WHERE p.booking.user = :user AND p.paymentState = :status")
    List<Payment> findByUserAndStatus(User user, PaymentState status);

    @Query("SELECT p FROM Payment p WHERE p.booking.service = :service")
    List<Payment> findByService(@Param("service") Service service);

    @Query("SELECT p FROM Payment p WHERE p.booking.service.provider = :provider")
    List<Payment> findByProvider(@Param("provider") Provider provider);

}

