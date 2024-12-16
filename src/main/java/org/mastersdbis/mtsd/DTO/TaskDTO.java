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
    private Integer bookingId;

    private Integer taskId;

    @NotNull(message = "Booking ID cannot be null.")
    private Integer bookingIdRef;

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    private String description;

    @NotNull(message = "Status cannot be null.")
    private TaskState status;

    @NotNull(message = "Due date cannot be null.")
    private LocalDate dueDate;
}