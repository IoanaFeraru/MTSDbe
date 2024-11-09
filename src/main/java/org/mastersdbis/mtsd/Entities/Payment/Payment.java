package org.mastersdbis.mtsd.Entities.Payment;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.Booking.Booking;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_gen")
    @SequenceGenerator(name = "payment_id_gen", sequenceName = "payment_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "Booking cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotNull(message = "Amount cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero.")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull(message = "Payment method cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentmethod", nullable = false)
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment state cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentstate", nullable = false)
    private PaymentState paymentState;

    @Column(name = "paymentdate")
    private LocalDate paymentDate;

    @FutureOrPresent(message = "Billing date must be in the present or future.")
    @Column(name = "datebilled")
    private LocalDate dateBilled;

    @Future(message = "Due date must be in the future.")
    @Column(name = "duedate")
    private LocalDate dueDate;

    @Override
    public Object getId() { return id; }
}
