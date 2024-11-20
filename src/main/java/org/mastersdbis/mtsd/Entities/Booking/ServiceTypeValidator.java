package org.mastersdbis.mtsd.Entities.Booking;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.mastersdbis.mtsd.Entities.Service.ServiceType;


public class ServiceTypeValidator implements ConstraintValidator<ValidBookingTime, Booking> {

    @Override
    public void initialize(ValidBookingTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {
        if (booking == null || booking.getService() == null) {
            return true;
        }

        if (booking.getService().getServiceType() == ServiceType.BOOKING) {
            return booking.getDueTime() != null;
        }

        return true;
    }
}
