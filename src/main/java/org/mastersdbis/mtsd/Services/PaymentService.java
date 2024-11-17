package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.mastersdbis.mtsd.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> checkDelayedPayments(User user) {
        // ToDO Implementare Logica
        return List.of();
    }

    public void paymentStatusManager(Payment payment) {
        // TODO Implementare logica
        paymentRepository.save(payment);
    }

    public void revertPayment (Payment payment) {
        // TODO implementare logica
        paymentRepository.save(payment);
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

    public List<Payment> findByBooking(Booking booking) {
        return paymentRepository.findByBooking(booking);
    }

    public List<Payment> findByUser(User user) {
        return paymentRepository.findByUser(user);
    }

    public List<Payment> findByUserStatus(User user, PaymentState status) {
        return paymentRepository.findByUserAndStatus(user, status);
    }

    public List<Payment> findByDate(Date date) {
        return paymentRepository.findByDate(date);
    }

    public List<Payment> findByPaymentState(PaymentState state) {
        return paymentRepository.findByPaymentState(state);
    }

    public List<Payment> findByUserCustom(User user) {
        return paymentRepository.findByUserCustom(user);
    }

    public List<Payment> findByPaymentDateBetween(Date start, Date end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    //TODO implementare exceptii
}
