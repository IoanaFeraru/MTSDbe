package org.mastersdbis.mtsd.Services;

import jakarta.validation.Valid;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Service.ServiceType;
import org.mastersdbis.mtsd.Entities.Payment.Payment;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.BookingRepository;
import org.mastersdbis.mtsd.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PaymentRepository paymentRepository, PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
    }

    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public void cancelBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking-ul nu poate fi null.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingCreationTime = booking.getDateCreated();
        long hoursDifference = Duration.between(bookingCreationTime, now).toHours();

        if (hoursDifference >= 24) {
            throw new IllegalStateException("Booking-ul poate fi anulat in primele 24 de ore.");
        }

        Payment payment = paymentService.findByBooking(booking)
                .orElseThrow(() -> new IllegalStateException("Nu există un payment asociat acestui booking."));

        paymentService.revertPayment(payment);

        booking.setBookingState(BookingState.CANCELED);
        booking.setDueDate(LocalDate.now());

        bookingRepository.save(booking);
    }

    public void completeBooking(Booking booking){
        if (booking.getBookingState()== BookingState.ACTIVE) {
            booking.setBookingState(BookingState.COMPLETED);
            bookingRepository.save(booking);
        } else
        {
            throw new IllegalStateException("Bookingul nu poate fi terminat daca acesta nu este activ.");
        }
    }

    public boolean checkDateOpen(Service service, LocalDate date, LocalTime time) {
        if (service.getServiceType() == ServiceType.BOOKING && time != null) {
            List<Booking> conflictingBookings = findByDueDateAndDueTime(date, time);
            return conflictingBookings.isEmpty();
        } else {
            long hoursUntilBooking = Duration.between(LocalDateTime.now(), date.atStartOfDay()).toHours();
            return hoursUntilBooking >= service.getMinimumBookingTime();
        }
    }

    public Booking addBooking(@Valid User user, Service service, double price, LocalDate dueDate, LocalTime time) {
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setService(service);
        newBooking.setPrice(price);

        boolean isOpen = false;

        if (service.getServiceType() == ServiceType.BOOKING && time != null) {
            isOpen = checkDateOpen(service, dueDate, time);
            newBooking.setDueTime(time);
            newBooking.setDueDate(dueDate);
        } else {
            isOpen = checkDateOpen(service, dueDate, null);
            newBooking.setDueDate(dueDate);
        }

        if (isOpen) {
            newBooking.setBookingState(BookingState.PENDING_PAYMENT);
        } else {
            throw new IllegalStateException("Slotul nu este disponibil pentru programarea dorită.");
        }
        return bookingRepository.save(newBooking);
    }

    public List<Booking> findByService(Service service) {
        return bookingRepository.findByService(service);
    }

    public List<Booking> findByClient(User client) {
        return bookingRepository.findByUser(client);
    }

    public List<Booking> findByProvider(Provider provider) {
        return bookingRepository.findByServiceProvider(provider);
    }

    public List<Booking> findByProviderAndStatus(Provider provider, BookingState status) {
        return bookingRepository.findByServiceProviderAndBookingState(provider, status);
    }

    public List<Booking> findByPeriod(LocalDate dateStart, LocalDate dateEnd) {
        return bookingRepository.findByDueDateBetween(dateStart, dateEnd);
    }

    public List<Booking> findByDueDateAndDueTime(LocalDate date, LocalTime time) {
        return bookingRepository.findByDueDateAndDueTime(date, time);
    }

    public List<Booking> findByUserAndBookingState(User user, BookingState state) {
        return bookingRepository.findByUserAndBookingState(user, state);
    }

    public List<Booking> findByUserAndBookingStateAndDueDate(User user, BookingState state, LocalDate dueDate) {
        return bookingRepository.findByUserAndBookingStateAndDueDate(user, state, dueDate);
    }
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Integer id) {
        return bookingRepository.findById(id);
    }
}
