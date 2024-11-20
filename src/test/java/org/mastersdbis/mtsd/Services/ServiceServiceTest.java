package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Service.*;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Entities.Payment.PaymentMethod;
import org.mastersdbis.mtsd.Entities.Payment.PaymentState;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServiceServiceTest {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @Test
    void addService() {
        Service newService = new Service();
        newService.setProvider(userService.findAllProviders().getFirst());
        newService.setName("Cleaning Service");
        newService.setDescription("Professional cleaning service for homes and offices.");
        newService.setDomain(ServiceDomain.DIVERSE);
        newService.setSubdomain(ServiceSubdomain.TRANSPORT_transport_persone);
        newService.setPrice(150.00);
        newService.setRegion(Region.BUZAU);
        newService.setMaterialsList(List.of("Detergent", "Cleaning cloths", "Vacuum cleaner"));
        newService.setAcceptedPaymentMethodsList(List.of(PaymentMethod.APPLE_PAY, PaymentMethod.CASH));
        newService.setServiceType(ServiceType.BOOKING);
        newService.setMinimumBookingTime(2);

        serviceService.saveService(newService);

        Service savedService = serviceService.findById(newService.getId());
        Assertions.assertNotNull(savedService, "Serviciul nou nu a fost salvat în baza de date.");
        System.out.println("Serviciul a fost adăugat cu succes: \n" + savedService);
    }

    @Test
    void modifyService() {
        // Arrange: Find an existing service
        Service existingService = serviceService.findById(1); // Assumes a service with ID 1 exists
        Assertions.assertNotNull(existingService, "Serviciul cu ID 1 nu a fost găsit în baza de date.");

        // Modify service details
        existingService.setName("Updated Cleaning Service");
        existingService.setDescription("Updated description for cleaning service.");
        existingService.setPrice(200.00);
        existingService.setMaterialsList(List.of("Updated detergent", "Microfiber cloths"));
        existingService.setMinimumBookingTime(3);

        // Act: Save the updated service
        serviceService.saveService(existingService);

        // Assert: Verify the service was updated correctly
        Service updatedService = serviceService.findById(existingService.getId());
        Assertions.assertNotNull(updatedService, "Serviciul modificat nu a fost găsit în baza de date.");
        Assertions.assertEquals("Updated Cleaning Service", updatedService.getName(), "Numele serviciului nu a fost actualizat corect.");
        Assertions.assertEquals(200.00, updatedService.getPrice(), "Prețul serviciului nu a fost actualizat corect.");
        System.out.println("Serviciul a fost modificat cu succes: " + updatedService);
    }
}
