package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.PaymentDTO;
import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.BookingService;
import org.mastersdbis.mtsd.Services.PaymentService;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//De testat tot :p
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ServiceService serviceService;
    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public PaymentController(PaymentService paymentService, ServiceService serviceService, UserService userService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.serviceService = serviceService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @PostMapping("/revert")
    public ResponseEntity<Void> revertPayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentDTO.toPayment();
        paymentService.revertPayment(payment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentDTO.toPayment();

        Booking booking = bookingService.findById(paymentDTO.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + paymentDTO.getBookingId()));

        payment.setBooking(booking);
        paymentService.processPayment(booking, payment);

        return ResponseEntity.ok("Payment adaugat cu succes");
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updatePayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentDTO.toPayment();
        paymentService.savePayment(payment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable int id) {
        Payment payment = paymentService.findById(id);
        if (payment != null) {
            paymentService.deletePayment(payment);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable int id) {
        Payment payment = paymentService.findById(id);
        if (payment != null) {
            return ResponseEntity.ok(PaymentDTO.fromPayment(payment));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/byBooking/{bookingId}")
    public ResponseEntity<PaymentDTO> findByBooking(@PathVariable int bookingId) {
        Booking booking = new Booking();
        booking.setId(bookingId);
        return paymentService.findByBooking(booking)
                .map(payment -> ResponseEntity.ok(PaymentDTO.fromPayment(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<PaymentDTO>> findByUser(@PathVariable int userId) {
        User user = new User();
        user.setId(userId);
        List<PaymentDTO> payments = paymentService.findByUser(user).stream()
                .map(PaymentDTO::fromPayment)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/byUserAndStatus/{userId}/{status}")
    public ResponseEntity<List<PaymentDTO>> findByUserStatus(@PathVariable int userId, @PathVariable PaymentState status) {
        User user = new User();
        user.setId(userId);
        List<PaymentDTO> payments = paymentService.findByUserStatus(user, status).stream()
                .map(PaymentDTO::fromPayment)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/byState/{state}")
    public ResponseEntity<List<PaymentDTO>> findByPaymentState(@PathVariable PaymentState state) {
        List<PaymentDTO> payments = paymentService.findByPaymentState(state).stream()
                .map(PaymentDTO::fromPayment)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByService(@PathVariable("serviceId") int serviceId) {
        Service service = serviceService.findById(serviceId);

        List<PaymentDTO> payments = paymentService.findByService(service).stream()
                .map(PaymentDTO::fromPayment)
                .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByProvider(@PathVariable("providerId") int providerId) {
        Provider provider = userService.findProviderByUser(userService.findById(providerId));

        List<PaymentDTO> payments = paymentService.findByProvider(provider).stream()
                .map(PaymentDTO::fromPayment)
                .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }
}
