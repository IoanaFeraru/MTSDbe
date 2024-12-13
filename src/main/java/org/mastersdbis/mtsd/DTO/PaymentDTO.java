package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Payment.PaymentMethod;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Integer id; // Unique identifier for the payment

    @NotNull(message = "Booking ID cannot be null.")
    private Integer bookingId; // Reference to the Booking entity

    @NotNull(message = "Amount cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero.")
    private Double amount; // Amount paid

    @NotNull(message = "Payment method cannot be null.")
    private PaymentMethod paymentMethod; // Enum representing payment methods

    @NotNull(message = "Payment state cannot be null.")
    private PaymentState paymentState; // Enum representing payment state

    private LocalDate paymentDate; // Date of payment
}