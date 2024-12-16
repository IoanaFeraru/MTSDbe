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
    private Integer id;

    @NotNull(message = "Booking ID cannot be null.")
    private Integer bookingId;

    @NotNull(message = "Amount cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero.")
    private Double amount;

    @NotNull(message = "Payment method cannot be null.")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment state cannot be null.")
    private PaymentState paymentState;

    private LocalDate paymentDate;
}