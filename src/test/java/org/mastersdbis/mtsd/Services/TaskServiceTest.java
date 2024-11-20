package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Task.TaskState;
import org.mastersdbis.mtsd.Services.TaskService;
import org.mastersdbis.mtsd.Services.BookingService;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @Test
    void saveTask() {
        Booking existingBooking = bookingService.findAllBookings().getFirst();

        Task task = new Task();
        task.setBooking(existingBooking);
        task.setDescription("Task description");
        task.setStatus(TaskState.DOING);
        task.setDuedate(LocalDate.now().plusDays(2));

        taskService.saveTask(task);

        Assertions.assertNotNull(task.getId(), "Task-ul nu a fost salvat corect.");
        Assertions.assertEquals(LocalDate.now().plusDays(2), task.getDuedate(), "Data limită nu este corectă.");

        System.out.println("Task salvat cu succes: " + task);
    }
}
