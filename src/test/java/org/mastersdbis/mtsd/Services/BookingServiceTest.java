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

        User user = userService.findByUsername("Stefan");
        Assertions.assertNotNull(user, "Utilizatorul nu a fost găsit în baza de date.");
        Service existingService = serviceService.findAll().getFirst();

        double price = 150.0;
        LocalDate dueDate = LocalDate.now().plusDays(5);
        LocalTime dueDateTime = LocalTime.now().minusHours(5);
        Booking booking = bookingService.addBooking(user, existingService, price, dueDate, dueDateTime);

        // Assert: Verify the booking was saved correctly
        Assertions.assertNotNull(booking, "Booking-ul nu a fost creat.");
        Assertions.assertEquals(dueDate, booking.getDueDate(), "Data booking-ului nu este corectă.");

        System.out.println("Booking adăugat cu succes: " + booking);
    }
}
