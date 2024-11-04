package org.mastersdbis.mtsd.Entities.Task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "task")
public class Task extends AbstractEntity {
    @EmbeddedId
    private TaskId id;

    @MapsId("bookingId")
    @NotNull(message = "Booking cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull(message = "Status cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskState status; // Înlocuiește câmpul `status` cu `TaskState`

    @NotNull(message = "Due date cannot be null.")
    @Column(name = "duedate")
    private LocalDate duedate;

    @Override
    public Object getId() { return id; }
}
