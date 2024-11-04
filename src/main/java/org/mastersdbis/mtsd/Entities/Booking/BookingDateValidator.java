package org.mastersdbis.mtsd.Entities.Booking;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookingDateValidator implements ConstraintValidator<ValidBookingDates, Booking> {

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {
        if (booking == null) {
            return true;
        }

        boolean isDueDateNull = (booking.getDueDate() == null);
        boolean isBookingDateNull = (booking.getBookingDate() == null);

        return (isDueDateNull ^ isBookingDateNull); // XOR
    }
}