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
        newService.setName("Cleaning Service2");
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
        Service existingService = serviceService.findAll().getFirst();
        Assertions.assertNotNull(existingService, "Serviciul nu a fost găsit în baza de date.");

        existingService.setPrice(200.00);

        serviceService.saveService(existingService);

        Service updatedService = serviceService.findById(existingService.getId());
        Assertions.assertEquals(200.00, updatedService.getPrice(), "Prețul serviciului nu a fost actualizat corect.");
        System.out.println("Serviciul a fost modificat cu succes: \n" + updatedService);
    }

    @Test
    void deleteService() {
        Service ForDeleteService = new Service();
        ForDeleteService.setProvider(userService.findAllProviders().getFirst());
        ForDeleteService.setName("Cleaning Service2");
        ForDeleteService.setDescription("Professional cleaning service for homes and offices.");
        ForDeleteService.setDomain(ServiceDomain.DIVERSE);
        ForDeleteService.setSubdomain(ServiceSubdomain.TRANSPORT_transport_persone);
        ForDeleteService.setPrice(420.00);
        ForDeleteService.setRegion(Region.BUZAU);
        ForDeleteService.setMaterialsList(List.of("Detergent", "Cleaning cloths", "Vacuum cleaner"));
        ForDeleteService.setAcceptedPaymentMethodsList(List.of(PaymentMethod.APPLE_PAY, PaymentMethod.CASH));
        ForDeleteService.setServiceType(ServiceType.BOOKING);
        ForDeleteService.setMinimumBookingTime(2);
        serviceService.saveService(ForDeleteService);
        Service savedService = serviceService.findById(ForDeleteService.getId());
        Assertions.assertNotNull(savedService, "Serviciul nou nu a fost salvat în baza de date.");

        serviceService.deleteService(ForDeleteService);
        Service deletedService = serviceService.findById(1);
        Assertions.assertNull(deletedService);
        System.out.println("Serviciul a fost sters din baza de date");
    }
}
