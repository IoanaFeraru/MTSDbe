package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.mastersdbis.mtsd.Repositories.BookingRepository;
import org.mastersdbis.mtsd.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    public void revertPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment-ul nu poate fi null.");
        }

        Payment refundPayment = new Payment();
        refundPayment.setAmount(payment.getAmount());
        refundPayment.setPaymentMethod(payment.getPaymentMethod());
        refundPayment.setPaymentState(PaymentState.REVERTED);
        refundPayment.setBooking(payment.getBooking());
        refundPayment.setPaymentDate(LocalDate.now());

        paymentRepository.save(refundPayment);
    }

    public void processPayment(Booking booking, Payment payment) {
        if (payment.getPaymentState() == PaymentState.ACCEPTED || payment.getPaymentState() == PaymentState.PENDING) {
            booking.setBookingState(BookingState.ACTIVE);
            paymentRepository.save(payment);
            bookingRepository.save(booking);
        } else {
            bookingRepository.delete(booking);
        }
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void deletePayment(Payment payment) {
        paymentRepository.delete(payment);
    }

    public Payment findById(int id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Optional<Payment> findByBooking(Booking booking) {
        return paymentRepository.findByBooking(booking);
    }

    public List<Payment> findByUser(User user) {
        return paymentRepository.findByUser(user);
    }

    public List<Payment> findByUserStatus(User user, PaymentState status) {
        return paymentRepository.findByUserAndStatus(user, status);
    }

    public List<Payment> findByPaymentState(PaymentState state) {
        return paymentRepository.findByPaymentState(state);
    }

    public List<Payment> findByUserCustom(User user) {
        return paymentRepository.findByUserCustom(user);
    }

    //TODO implementare exceptii
}
