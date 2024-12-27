package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Payment.PaymentMethod;
import org.mastersdbis.mtsd.Entities.Service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Integer id;

    @NotNull(message = "Name cannot be null.")
    @Size(max = 100, message = "Name must not exceed 100 characters.")
    private String name;

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    private String description;

    @NotNull(message = "Domain cannot be null.")
    private ServiceDomain domain;

    private ServiceSubdomain subdomain;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @NotNull(message = "Price cannot be null.")
    private Double price;

    @NotNull(message = "Region cannot be null.")
    private Region region;

    private List<String> materials;

    @NotNull(message = "Active state cannot be null.")
    private Boolean active;

    private List<PaymentMethod> acceptedPaymentMethods;

    @NotNull(message = "Booking type cannot be null.")
    private ServiceType serviceType;

    @NotNull(message = "Minimum booking time cannot be null.")
    private Integer minimumBookingTime;

    public List<PaymentMethod> parsePaymentMethods(String acceptedPaymentMethods) {
        if (acceptedPaymentMethods != null && !acceptedPaymentMethods.isEmpty()) {
            return Arrays.stream(acceptedPaymentMethods.split(",\\s*"))
                    .map(method -> {
                        try {
                            return PaymentMethod.valueOf(method.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static ServiceDTO fromService(Service service) {
        if (service == null) {
            return null;
        }
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setName(service.getName());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setDomain(service.getDomain());
        serviceDTO.setSubdomain(service.getSubdomain());
        serviceDTO.setPrice(service.getPrice());
        serviceDTO.setRegion(service.getRegion());
        serviceDTO.setMaterials(service.getMaterialsList());
        serviceDTO.setActive(service.getActive());
        serviceDTO.setServiceType(service.getServiceType());
        serviceDTO.setMinimumBookingTime(service.getMinimumBookingTime());
        serviceDTO.setAcceptedPaymentMethods(serviceDTO.parsePaymentMethods(service.getAcceptedPaymentMethods()));
        return serviceDTO;
    }

}