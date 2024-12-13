package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Payment.PaymentMethod;
import org.mastersdbis.mtsd.Entities.Service.Region;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.Service.ServiceSubdomain;
import org.mastersdbis.mtsd.Entities.Service.ServiceType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Integer id; // Unique identifier

    @NotNull(message = "Provider ID cannot be null.")
    private Integer providerId; // Reference to provider ID

    @NotNull(message = "Name cannot be null.")
    @Size(max = 100, message = "Name must not exceed 100 characters.")
    private String name;

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    private String description;

    @NotNull(message = "Domain cannot be null.")
    private ServiceDomain domain;

    private ServiceSubdomain subdomain; // Optional subdomain (can be null)

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @NotNull(message = "Price cannot be null.")
    private Double price;

    @NotNull(message = "Region cannot be null.")
    private Region region;

    private List<String> materials; // Materials as a list of strings

    @NotNull(message = "Active state cannot be null.")
    private Boolean active;

    private List<PaymentMethod> acceptedPaymentMethods; // Payment methods as a list of enums

    @NotNull(message = "Booking type cannot be null.")
    private ServiceType serviceType;

    @NotNull(message = "Minimum booking time cannot be null.")
    private Integer minimumBookingTime;
}