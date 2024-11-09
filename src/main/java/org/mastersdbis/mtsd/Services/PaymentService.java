package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.mastersdbis.mtsd.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
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
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void updatePayment(Payment payment) {
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

    public List<Payment> findByPaymentstate(PaymentState state) {
        return paymentRepository.findBypaymentstate(state);
    }

    public List<Payment> findByUserCustom(User user) {
        return paymentRepository.findByUserCustom(user);
    }

    //TODO implementare exceptii
}
