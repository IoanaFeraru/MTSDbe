package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Integer id;

    @NotNull(message = "User cannot be null.")
    private int userId;

    @NotNull(message = "Service cannot be null.")
    private Integer serviceId;

    @FutureOrPresent(message = "Due date must be in the present or future.")
    private LocalDate dueDate;

    private LocalTime dueTime;

    @Size(max = 255, message = "Delivery address must not exceed 255 characters.")
    private String deliveryAddress;

    @NotNull(message = "Price cannot be null.")
    private Double price;

    @NotNull(message = "Booking state cannot be null.")
    private BookingState bookingState;

    public static BookingDTO fromBooking(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setUserId(booking.getUser() != null ? booking.getUser().getId() : null);
        bookingDTO.setServiceId(booking.getService() != null ? booking.getService().getId() : null);
        bookingDTO.setDueDate(booking.getDueDate());
        bookingDTO.setDueTime(booking.getDueTime());
        bookingDTO.setDeliveryAddress(booking.getDeliveryAddress());
        bookingDTO.setPrice(booking.getPrice());
        bookingDTO.setBookingState(booking.getBookingState());
        return bookingDTO;
    }

    public static Booking toBooking(BookingDTO bookingDTO, User user, Service service) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setUser(user); // Logica asta se face in controller
        booking.setService(service); // Si asta tot in controller
        booking.setDueDate(bookingDTO.getDueDate());
        booking.setDueTime(bookingDTO.getDueTime());
        booking.setDeliveryAddress(bookingDTO.getDeliveryAddress());
        booking.setPrice(bookingDTO.getPrice());
        booking.setBookingState(bookingDTO.getBookingState());
        return booking;
    }
}