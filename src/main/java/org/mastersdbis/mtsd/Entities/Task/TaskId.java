package org.mastersdbis.mtsd.Entities.Task;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TaskId implements java.io.Serializable {
    private static final long serialVersionUID = 1998497350070644348L;

    @NotNull
    @Column(name = "tasknumber", nullable = false)
    private Integer taskNumber;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaskId entity = (TaskId) o;
        return Objects.equals(this.taskNumber, entity.taskNumber) &&
                Objects.equals(this.bookingId, entity.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskNumber, bookingId);
    }

    @Override
    public String toString() {
        return "TaskId{" +
                "tasknumber=" + taskNumber +
                ", booking_id=" + bookingId +
                '}';
    }
}
