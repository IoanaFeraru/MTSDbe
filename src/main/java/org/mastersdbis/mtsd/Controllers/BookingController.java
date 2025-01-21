package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.BookingDTO;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.BookingService;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//De testat tot
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ServiceService serviceService;
    private final UserService userService;

    @Autowired
    public BookingController(BookingService bookingService, ServiceService serviceService, UserService userService) {
        this.bookingService = bookingService;
        this.serviceService = serviceService;
        this.userService = userService;
    }

    private List<BookingDTO> mapBookingsToDTOs(List<Booking> bookings) {
        return bookings.stream().map(BookingDTO::fromBooking).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<String> addBooking(@RequestBody BookingDTO bookingDTO) {
        try {
            User user = userService.findByUsername(bookingDTO.getUsername());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found.");
            }

            Service service = serviceService.findById(bookingDTO.getServiceId());
            if (service == null) {
                return ResponseEntity.badRequest().body("Service not found.");
            }

            Booking newBooking = BookingDTO.toBooking(bookingDTO, user, service);
            bookingService.updateBooking(newBooking);

            return ResponseEntity.ok("Booking added successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable int id, @RequestBody BookingDTO bookingDTO) {
        Booking existingBooking = bookingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        if (existingBooking == null) {
            return ResponseEntity.status(404).body("Booking not found.");
        }

        try {
            existingBooking.setDueDate(bookingDTO.getDueDate());
            existingBooking.setDueTime(bookingDTO.getDueTime());
            existingBooking.setDeliveryAddress(bookingDTO.getDeliveryAddress());
            existingBooking.setPrice(bookingDTO.getPrice());
            existingBooking.setBookingState(bookingDTO.getBookingState());

            bookingService.updateBooking(existingBooking);

            return ResponseEntity.ok("Booking updated successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable int id) {
        Booking booking = bookingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        if (booking == null) {
            return ResponseEntity.status(404).body("Booking not found.");
        }

        try {
            bookingService.cancelBooking(booking);
            return ResponseEntity.ok("Booking canceled successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        List<Booking> bookings = bookingService.findByClient(user);
        return ResponseEntity.ok(mapBookingsToDTOs(bookings));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByService(@PathVariable int serviceId) {
        Service service = serviceService.findById(serviceId);
        if (service == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<Booking> bookings = bookingService.findByService(service);
        return ResponseEntity.ok(mapBookingsToDTOs(bookings));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer bookingId) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookingDTO.fromBooking(booking));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Map<String, String>> completeBooking(@PathVariable int id) {
        Booking booking = bookingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        try {
            bookingService.completeBooking(booking);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Booking completed successfully.");

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/provider/{username}")
    public ResponseEntity<List<BookingDTO>> getBookingsByProvider(@PathVariable String username) {
        User user = userService.findByUsername(username);
        Provider provider = userService.findProviderByUser(user);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Booking> bookings = bookingService.findByProvider(provider);
        return ResponseEntity.ok(mapBookingsToDTOs(bookings));
    }

}
