package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Task.TaskState;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Integer bookingId; // Part of the composite ID (reference to Booking ID)

    private Integer taskId; // Task-specific unique identifier (if applicable)

    @NotNull(message = "Booking ID cannot be null.")
    private Integer bookingIdRef; // Reference to Booking entity

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    private String description;

    @NotNull(message = "Status cannot be null.")
    private TaskState status;

    @NotNull(message = "Due date cannot be null.")
    private LocalDate dueDate;
}