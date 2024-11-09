package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void cancelBooking(Booking booking){
        //TODO implementare logica
        bookingRepository.save(booking);
    }

    public void completeBooking(Booking booking){
        //TODO implementare logica
        bookingRepository.save(booking);
    }

    public boolean checkDateOpen(Booking booking) {
        //TODO modificare/verificare logica
        LocalDate bookingDate = booking.getDueDate();

        List<Booking> conflictingBookings = bookingRepository.findByDueDate(bookingDate);
        return conflictingBookings.isEmpty();
    }


    public Booking purchaseService(User user, Service service, double negotiatedPrice) {
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setService(service);
        newBooking.setPrice(negotiatedPrice);
        if(checkDateOpen(newBooking)) {
            newBooking.setBookingState(BookingState.ACTIVE);
            //TODO implementare faptul ca se creaza payment
        }
        else
            cancelPurchase(newBooking);
        return bookingRepository.save(newBooking);
    }

    public void cancelPurchase(Booking booking) {
        // ToDo ??nu stiu daca mai trb ceva aici
        booking.setBookingState(BookingState.CANCELED);
        bookingRepository.save(booking);
    }

    public void setPriceNegotiation(User client, User provider, double proposedPrice) {
        // ToDO implementare logica
    }

    public List<Booking> findByService(Service service) {
        return bookingRepository.findByService(service);
    }

    public List<Booking> findByClient(User client) {
        return bookingRepository.findByUser(client);
    }

    public List<Booking> findByProvider(User provider) {
        return bookingRepository.findByServiceProvider(provider);
    }

    public List<Booking> findByProviderAndStatus(User provider, BookingState status) {
        return bookingRepository.findByServiceProviderAndBookingState(provider, status);
    }

    public List<Booking> findByDate(LocalDate dateStart, LocalDate dateEnd) {
        return bookingRepository.findByDueDateBetween(dateStart, dateEnd);
    }

    public List<Booking> findByUserAndBookingState(User user, BookingState state) {
        return bookingRepository.findByUserAndBookingState(user, state);
    }

    public List<Booking> findByUserAndBookingStateAndDueDate(User user, BookingState state, LocalDate dueDate) {
        return bookingRepository.findByUserAndBookingStateAndDueDate(user, state, dueDate);
    }
    //TODO implementare exceptii
}
