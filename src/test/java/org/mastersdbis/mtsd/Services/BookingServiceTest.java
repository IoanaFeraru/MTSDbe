package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.BookingService;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceService serviceService;

    @Test
    void addBooking() {

        User user = userService.findByUsername("stefanTest");
        Assertions.assertNotNull(user, "Utilizatorul nu a fost găsit în baza de date.");
        Service existingService = serviceService.findAll().getFirst();

        LocalDate dueDate = LocalDate.now().plusDays(10);
        LocalTime dueDateTime = LocalTime.now().minusHours(5);
        Booking booking = bookingService.addBooking(user, existingService, existingService.getPrice(), dueDate, dueDateTime);

        Assertions.assertNotNull(booking, "Booking-ul nu a fost creat.");
        assertEquals(dueDate, booking.getDueDate(), "Data booking-ului nu este corectă.");

        System.out.println("Booking adăugat cu succes: " + booking);
    }

    @Test
    void updateBooking() {
        Booking existingBooking = bookingService.findAllBookings().getLast();
        existingBooking.setDeliveryAddress("Str. Dealul Zorilor");
        bookingService.updateBooking(existingBooking);
        assertEquals("Str. Dealul Zorilor", existingBooking.getDeliveryAddress(),"Adresa nu a fost actualizata");
        System.out.println("Adresa de booking a fost actualizata cu succes!");
    }
    @Test
    void cancelBooking() {
        Booking existingBooking = bookingService.findAllBookings().getLast();
        bookingService.cancelBooking(existingBooking);

        assertEquals(BookingState.CANCELED, bookingService.findAllBookings().getLast().getBookingState(), "Bookingul nu a fost canceled");

        System.out.println("Bookingul a fost canceled" + bookingService.findAllBookings().getFirst().toString());
    }
    @Test
    void completeBooking() {
        Booking existingBooking = bookingService.findAllBookings().getLast();
        bookingService.completeBooking(existingBooking);
        assertEquals(BookingState.COMPLETED, bookingService.findAllBookings().getLast().getBookingState(), "Bookingul nu a fost completed");
        System.out.println("Bookingul a fost completed" + bookingService.findAllBookings().getFirst().toString());
    }
}
