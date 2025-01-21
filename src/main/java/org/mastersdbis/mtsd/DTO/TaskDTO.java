package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Task.TaskId;
import org.mastersdbis.mtsd.Entities.Task.TaskState;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Integer taskNumber;
    private Integer bookingId;
    private String description;
    private TaskState status;

    @NotNull(message = "Due date cannot be null.")
    private LocalDate dueDate;

    public Task toTask() {
        Task task = new Task();
        TaskId taskId = new TaskId(taskNumber, bookingId);
        task.setId(taskId);
        task.setDescription(description);
        task.setStatus(status);
        task.setDuedate(dueDate);
        return task;
    }

    public static TaskDTO fromTask(Task task) {
        TaskDTO dto = new TaskDTO();
        if (task.getId() instanceof TaskId taskId) {
            dto.setTaskNumber(taskId.getTaskNumber());
            dto.setBookingId(taskId.getBookingId());
        } else {
            throw new IllegalArgumentException("Unexpected ID type.");
        }
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDuedate());
        return dto;
    }
}