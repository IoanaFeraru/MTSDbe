package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.ServiceDTO;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.mastersdbis.mtsd.Entities.Service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final UserService userService;

    @Autowired
    public ServiceController(ServiceService serviceService, UserService userService) {
        this.serviceService = serviceService;
        this.userService = userService;
    }

    private List<ServiceDTO> mapServicesToDTOs(List<Service> services) {
        return services.stream().map(ServiceDTO::fromService).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<String> saveService(@RequestBody ServiceDTO serviceDTO) {
        try {
            String username = serviceDTO.getUsername();

            Provider provider = userService.findProviderByUser(userService.findByUsername(username));

            if (provider == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Provider not found for username: " + username);
            }

            Service service = new Service();
            service.setName(serviceDTO.getName());
            service.setDescription(serviceDTO.getDescription());
            service.setDomain(serviceDTO.getDomain());
            service.setSubdomain(serviceDTO.getSubdomain());
            service.setPrice(serviceDTO.getPrice());
            service.setRegion(serviceDTO.getRegion());
            service.setActive(true);
            service.setAcceptedPaymentMethods(serviceDTO.getAcceptedPaymentMethods().toString());
            service.setServiceType(serviceDTO.getServiceType());
            service.setMinimumBookingTime(serviceDTO.getMinimumBookingTime());
            service.setProvider(provider);

            serviceService.saveService(service);

            return ResponseEntity.ok("Service saved successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteService(@PathVariable int id) {
        Service service = serviceService.findById(id);
        if (service != null) {
            serviceService.deleteService(service);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Service deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Service not found.");
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable int id) {
        Service service = serviceService.findById(id);
        if (service != null) {
            ServiceDTO serviceDTO = ServiceDTO.fromService(service);
            return ResponseEntity.ok(serviceDTO);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/provider/{username}")
    public ResponseEntity<List<ServiceDTO>> getServicesByProvider(@PathVariable String username) {
        Provider provider = userService.findProviderByUser(userService.findByUsername(username));
        if (provider == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<Service> services = serviceService.findByProviderAndActiveTrue(provider);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceDTO>> getActiveServices() {
        List<Service> services = serviceService.findByActiveTrue();
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ServiceDTO>> searchServices(
            @RequestParam String searchTerm) {
        List<Service> services = serviceService.searchServices(searchTerm);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateService(@PathVariable int id, @RequestBody Service service) {
        Service existingService = serviceService.findById(id);
        if (existingService == null) {
            return ResponseEntity.status(404).body("Service not found.");
        }

        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setDomain(service.getDomain());
        existingService.setSubdomain(service.getSubdomain());
        existingService.setPrice(service.getPrice());
        existingService.setRegion(service.getRegion());
        existingService.setActive(service.getActive());
        existingService.setMinimumBookingTime(service.getMinimumBookingTime());

        try {
            serviceService.saveService(existingService);
            return ResponseEntity.ok("Service updated successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //De testat
    @GetMapping("/domain/{domain}")
    public ResponseEntity<List<ServiceDTO>> getServicesByDomain(@PathVariable ServiceDomain domain) {
        List<Service> services = serviceService.findByDomain(domain);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    //De testat
    @GetMapping("/subdomain/{subdomain}")
    public ResponseEntity<List<ServiceDTO>> getServicesBySubdomain(@PathVariable ServiceSubdomain subdomain) {
        List<Service> services = serviceService.findBySubdomain(subdomain);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    //De testat
    @GetMapping("/region/{region}")
    public ResponseEntity<List<ServiceDTO>> getServicesByRegion(@PathVariable Region region) {
        List<Service> services = serviceService.findByRegion(region);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }
}
