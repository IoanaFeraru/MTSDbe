package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByBooking(Booking booking);

    @Query("SELECT p FROM Payment p WHERE p.booking.user = :user")
    List<Payment> findByUser(@Param("user") User user);

    List<Payment> findBypaymentstate(PaymentState paymentState);

    @Query("SELECT p FROM Payment p WHERE p.booking.user = :user")
    List<Payment> findByUserCustom(User user);

    @Query("SELECT p FROM Payment p WHERE p.booking.user = :user AND p.paymentstate = :status")
    List<Payment> findByUserAndStatus(User user, PaymentState status);

    @Query("SELECT p FROM Payment p WHERE p.dateBilled = :date")
    List<Payment> findByDate(Date date);
}

