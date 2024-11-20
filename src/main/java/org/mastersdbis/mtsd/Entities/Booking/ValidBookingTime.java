package org.mastersdbis.mtsd.Entities.Booking;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ServiceTypeValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingTime {

    String message() default "Due time must not be null for a booking service.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}