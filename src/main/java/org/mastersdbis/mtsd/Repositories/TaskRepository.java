package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Task.Task;
import org.mastersdbis.mtsd.Entities.Booking.Booking;
import org.mastersdbis.mtsd.Entities.Task.TaskId;
import org.mastersdbis.mtsd.Entities.Task.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, TaskId> {

    Task findById_BookingIdAndId_taskNumber(Integer bookingId, Integer taskNumber);

    List<Task> findByBooking(Booking booking);

    @Query("SELECT t FROM Task t WHERE t.booking = :booking AND t.duedate = :dueDate")
    List<Task> findByBookingFilterDueDate(Booking booking, Date dueDate);

    @Query("SELECT t FROM Task t WHERE t.booking = :booking AND t.status = :status")
    List<Task> findByBookingFilterStatus(Booking booking, TaskState status);
}
