package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Task.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

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
    void addTask() {
        Booking existingBooking = bookingService.findAllBookings().getLast();

        Task task = new Task();
        task.setBooking(existingBooking);
        task.setDescription("Task description");
        task.setStatus(TaskState.DOING);
        task.setDuedate(LocalDate.now().plusDays(2));

        taskService.addTask(task);

        Assertions.assertNotNull(task.getId(), "Task-ul nu a fost salvat corect.");
        Assertions.assertEquals(LocalDate.now().plusDays(2), task.getDuedate(), "Data limită nu este corectă.");

        System.out.println("Task salvat cu succes: " + task);
    }

    @Test
    void updateTask() {
        Booking existingBooking = bookingService.findAllBookings().getLast();
        Task task = taskService.findByBooking(existingBooking).getLast();
        task.setDescription("descriere");
        taskService.updateTask(task);
        Assertions.assertEquals("descriere", taskService.findByBooking(existingBooking).getLast().getDescription(),"Descrierea nu a fost modificata");
        System.out.println("Task modificat cu succes: " + task);
    }

    @Test
    void deleteTask() {
        Booking existingBooking = bookingService.findAllBookings().getLast();
        Task task = taskService.findByBooking(existingBooking).getLast();
        taskService.deleteTask(task);
        List<Task> tasksAfterDeletion = taskService.findByBooking(existingBooking);
        Assertions.assertTrue(tasksAfterDeletion.isEmpty() || !tasksAfterDeletion.contains(task),
                "Task-ul nu a fost șters!");
        System.out.println("Taskul a fost sters!");
    }

    @Test
    void manageTaskState() {
        Booking existingBooking = bookingService.findAllBookings().getLast();
        Task task = taskService.findByBooking(existingBooking).getLast();
        taskService.manageTaskState(task, TaskState.DONE);
        Assertions.assertEquals(TaskState.DONE,taskService.findByBooking(existingBooking).getLast().getStatus(),"Status nu a fost modificat");
        System.out.println("Statusul taskului a fost modificat! " + taskService.findByBooking(existingBooking).getLast());
    }

}
