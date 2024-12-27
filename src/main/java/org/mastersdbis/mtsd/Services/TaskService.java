package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Task.TaskId;
import org.mastersdbis.mtsd.Entities.Task.TaskState;
import org.mastersdbis.mtsd.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkOverdueTasks() {
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            if (task.getStatus() != TaskState.OVERDUE && task.getDuedate().isBefore(LocalDate.now())) {
                task.setStatus(TaskState.OVERDUE);
                taskRepository.save(task);
            }
        }
    }

    public void manageTaskState(Task task, TaskState state) {
        task.setStatus(state);
        taskRepository.save(task);
    }

    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    public void addTask(Task task) {
        if (task.getId() == null) {
            task.setId(new TaskId());
        }

        Integer maxTaskNumber = taskRepository.findMaxTaskNumberByBookingId(task.getBooking().getId());
        int taskNumber;
        if(maxTaskNumber == null) {
            taskNumber = 1;
        }
        else {
            taskNumber = maxTaskNumber + 1;
        }
        TaskId newTaskId = new TaskId(taskNumber, task.getBooking().getId());
        task.setId(newTaskId);
        taskRepository.save(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public Task findById(Booking booking, int taskNr) {
        return taskRepository.findById_BookingIdAndId_taskNumber(booking.getId(), taskNr);
    }

    public List<Task> findByBooking(Booking booking) {
        return taskRepository.findByBooking(booking);
    }

    public List<Task> findByBookingFilterDueDate(Booking booking, Date dueDate) {
        return taskRepository.findByBookingFilterDueDate(booking, dueDate);
    }

    public List<Task> findByBookingFilterStatus(Booking booking, TaskState status) {
        return taskRepository.findByBookingFilterStatus(booking, status);
    }
}
