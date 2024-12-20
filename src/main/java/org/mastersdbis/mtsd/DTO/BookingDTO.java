package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Booking.BookingState;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Integer id;

    @NotNull(message = "User cannot be null.")
    private Integer userId;

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
}