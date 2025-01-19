package org.mastersdbis.mtsd.Entities.Service;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.Payment.PaymentMethod;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service")
public class Service extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_id_gen")
    @SequenceGenerator(name = "service_id_gen", sequenceName = "service_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "Provider cannot be null.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", nullable = false, referencedColumnName = "id")
    private Provider provider;

    @NotNull(message = "Name cannot be null.")
    @Size(max = 100, message = "Name must not exceed 100 characters.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull(message = "Domain cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "domain")
    private ServiceDomain domain;

    @Enumerated(EnumType.STRING)
    @Column(name = "subdomain")
    private ServiceSubdomain subdomain;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @NotNull(message = "Price cannot be null.")
    @Column(name = "price")
    private Double price;

    @NotNull(message = "Region cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "region", length = 50)
    private Region region;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "accepted_payment_methods")
    private String acceptedPaymentMethods;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Booking type cannot be null.")
    @Column(name = "servicetype", length = 20)
    private ServiceType serviceType;

    @Column(name = "minimum_booking_time", nullable = false)
    private Integer minimumBookingTime;

    @Override
    public Integer getId() { return id; }

    // Obține lista metodelor de plată acceptate pe baza enumurilor PaymentMethod
    public List<PaymentMethod> getAcceptedPaymentMethodsList() {
        if (acceptedPaymentMethods != null && !acceptedPaymentMethods.isEmpty()) {
            return Arrays.stream(acceptedPaymentMethods.split(",\\s*"))
                    .map(PaymentMethod::valueOf)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // Un string ce salveaza metodele de plata acceptate separate cu virgula
    public void setAcceptedPaymentMethodsList(List<PaymentMethod> paymentMethodsList) {
        this.acceptedPaymentMethods = paymentMethodsList.stream()
                .map(PaymentMethod::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", providerId=" + (provider != null ? provider.getId() : "null") +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", domain=" + domain +
                ", subdomain=" + subdomain +
                ", price=" + price +
                ", region=" + region +
                ", active=" + active +
                ", acceptedPaymentMethods=" + getAcceptedPaymentMethodsList() +
                ", serviceType=" + getServiceType() +
                ", minimumBookingTime=" + minimumBookingTime +
                '}';
    }
}
