package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.Payment.PaymentMethod;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Test
    void processPayment() {
        Optional<Booking> optionalBooking = bookingService.findById(5);

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setPaymentDate(LocalDate.now());
            payment.setAmount(booking.getPrice());
            payment.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
            payment.setPaymentState(PaymentState.ACCEPTED);

            paymentService.processPayment(booking, payment);

            Assertions.assertNotNull(paymentService.findByBooking(booking), "Paymentul nu a fost creat");

            System.out.println("Paymentul a fost creat" + payment);
        } else {
            Assertions.fail("Booking not found");
        }
    }

}
