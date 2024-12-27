package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.ServiceDTO;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.mastersdbis.mtsd.Entities.Service.*;

import java.util.List;
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
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Provider provider = userService.findProviderByUser(userService.findByUsername(userDetails.getUsername()));

            Service service = new Service();
            service.setName(serviceDTO.getName());
            service.setDescription(serviceDTO.getDescription());
            service.setDomain(serviceDTO.getDomain());
            service.setSubdomain(serviceDTO.getSubdomain());
            service.setPrice(serviceDTO.getPrice());
            service.setRegion(serviceDTO.getRegion());
            service.setMaterials(String.valueOf(serviceDTO.getMaterials()));
            service.setActive(serviceDTO.getActive());
            service.setAcceptedPaymentMethods(serviceDTO.getAcceptedPaymentMethods().toString());
            service.setServiceType(serviceDTO.getServiceType());
            service.setMinimumBookingTime(serviceDTO.getMinimumBookingTime());
            service.setProvider(provider);

            serviceService.saveService(service);

            return ResponseEntity.ok("Service saved successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable int id) {
        Service service = serviceService.findById(id);
        if (service != null) {
            serviceService.deleteService(service);
            return ResponseEntity.ok("Service deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Service not found.");
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

    //De testat
    @GetMapping("/provider/{username}")
    public ResponseEntity<List<ServiceDTO>> getServicesByProvider(@PathVariable String username) {
        Provider provider = userService.findProviderByUser(userService.findByUsername(username));
        if (provider == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<Service> services = serviceService.findByProviderAndActiveTrue(provider);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    //De testat
    @GetMapping("/active")
    public ResponseEntity<List<ServiceDTO>> getActiveServices() {
        List<Service> services = serviceService.findByActiveTrue();
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    //De testat
    @GetMapping("/search")
    public ResponseEntity<List<ServiceDTO>> searchServices(
            @RequestParam(required = false) Provider provider,
            @RequestParam(required = false) ServiceDomain domain,
            @RequestParam(required = false) ServiceSubdomain subdomain,
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) Double start,
            @RequestParam(required = false) Double end) {

        List<Service> services = serviceService.searchServices(provider, domain, subdomain, region, start, end);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }

    //De testat
    @PutMapping("/{id}")
    public ResponseEntity<String> updateService(@PathVariable int id, @RequestBody Service service) {
        Service existingService = serviceService.findById(id);
        if (existingService == null) {
            return ResponseEntity.status(404).body("Service not found.");
        }

        existingService.setProvider(service.getProvider());
        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setDomain(service.getDomain());
        existingService.setSubdomain(service.getSubdomain());
        existingService.setPrice(service.getPrice());
        existingService.setRegion(service.getRegion());
        existingService.setMaterials(service.getMaterials());
        existingService.setActive(service.getActive());
        existingService.setAcceptedPaymentMethods(service.getAcceptedPaymentMethods());
        existingService.setServiceType(service.getServiceType());
        existingService.setMinimumBookingTime(service.getMinimumBookingTime());

        existingService.setMaterialsList(service.getMaterialsList());
        existingService.setAcceptedPaymentMethodsList(service.getAcceptedPaymentMethodsList());

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

    //De testat
    @GetMapping("/provider")
    public ResponseEntity<List<ServiceDTO>> getServicesByProvider() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Provider provider = userService.findProviderByUser(userService.findByUsername(userDetails.getUsername()));
        if (provider == null) {
            return ResponseEntity.status(403).body(null);
        }

        List<Service> services = serviceService.findByProvider(provider);
        return ResponseEntity.ok(mapServicesToDTOs(services));
    }
}
