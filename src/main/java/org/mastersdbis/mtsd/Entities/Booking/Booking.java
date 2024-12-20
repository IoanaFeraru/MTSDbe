package org.mastersdbis.mtsd.Entities.Booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
@ValidBookingTime
public class Booking extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_gen")
    @SequenceGenerator(name = "booking_id_gen", sequenceName = "booking_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "User cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Service cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @FutureOrPresent(message = "Due date must be in the present or future.")
    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "booking_time")
    private LocalTime dueTime;

    @Size(max = 255, message = "Delivery address must not exceed 255 characters.")
    @Column(name = "deliveryaddress")
    private String deliveryAddress;

    @NotNull(message = "Price cannot be null.")
    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Booking state cannot be null.")
    @Column(name = "bookingstate", length = 20)
    private BookingState bookingState;

    @Override
    public Integer getId() { return id; }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "null") +
                ", service=" + (service != null ? service.getId() : "null") +
                ", dueDate=" + dueDate +
                ", dueTime=" + dueTime +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", price=" + price +
                ", bookingState=" + bookingState +
                '}';
    }
}
