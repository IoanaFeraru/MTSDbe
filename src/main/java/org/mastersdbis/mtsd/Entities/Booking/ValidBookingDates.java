package org.mastersdbis.mtsd.Entities.Booking;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookingDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingDates {
    String message() default "One of dueDate or bookingDate must be null, but not both.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}