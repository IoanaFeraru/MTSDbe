package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Task.TaskState;
import org.mastersdbis.mtsd.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void manageTaskState(Task task) {
        // TODO implementare logica
        taskRepository.save(task);
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    public void updateTask(Task task) {
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

    //TODO implementare exceptii
}
