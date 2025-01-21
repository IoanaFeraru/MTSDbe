package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.TaskDTO;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Task.TaskState;
import org.mastersdbis.mtsd.Services.BookingService;
import org.mastersdbis.mtsd.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final BookingService bookingService;

    @Autowired
    public TaskController(TaskService taskService, BookingService bookingService) {
        this.taskService = taskService;
        this.bookingService = bookingService;
    }

    @PutMapping("/manageState")
    public ResponseEntity<Void> manageTaskState(@RequestParam Integer taskNumber,
                                                @RequestParam Integer bookingId,
                                                @RequestParam TaskState state) {
        taskService.manageTaskState(taskNumber, bookingId, state);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskDTO.toTask();
        taskService.updateTask(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskDTO.toTask();

        Booking booking = bookingService.findById(taskDTO.getBookingId())
                .orElseThrow(NoSuchElementException::new);

        task.setBooking(booking);
        taskService.addTask(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskDTO.toTask();
        taskService.deleteTask(task);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<TaskDTO> findById(@RequestParam Integer bookingId, @RequestParam Integer taskNr) {
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking found for ID: " + bookingId));
        Task task = taskService.findById(booking, taskNr);
        TaskDTO taskDTO = TaskDTO.fromTask(task);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping("/findByBooking")
    public ResponseEntity<List<TaskDTO>> findByBooking(@RequestParam Integer bookingId) {
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking found for ID: " + bookingId));
        List<TaskDTO> tasks = taskService.findByBooking(booking).stream()
                .map(TaskDTO::fromTask)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/findByBookingAndDueDate")
    public ResponseEntity<List<TaskDTO>> findByBookingFilterDueDate(@RequestParam Integer bookingId, @RequestParam LocalDate dueDate) {
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking found for ID: " + bookingId));
        List<TaskDTO> tasks = taskService.findByBookingFilterDueDate(booking, java.sql.Date.valueOf(dueDate)).stream()
                .map(TaskDTO::fromTask)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/findByBookingAndStatus")
    public ResponseEntity<List<TaskDTO>> findByBookingFilterStatus(@RequestParam Integer bookingId, @RequestParam TaskState status) {
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking found for ID: " + bookingId));
        List<TaskDTO> tasks = taskService.findByBookingFilterStatus(booking, status).stream()
                .map(TaskDTO::fromTask)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }
}