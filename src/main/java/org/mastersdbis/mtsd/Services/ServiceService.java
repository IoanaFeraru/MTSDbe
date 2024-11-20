package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Service.Region;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.Service.ServiceSubdomain;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void saveService(Service service) {
        Provider provider = service.getProvider();
        if (provider == null || provider.getValidationStatus() != ValidationStatus.APPROVED) {
            throw new IllegalStateException("Serviciul nu poate fi salvat deoarece providerul nu este aprobat.");
        }
        serviceRepository.save(service);
    }

    public void deleteService(Service service) {
        serviceRepository.delete(service);
    }

    public Service findById(int id) {
        Optional<Service> service = serviceRepository.findById(id);
        return service.orElse(null);
    }

    public List<Service> findByProvider(Provider provider) {
        return serviceRepository.findByProvider(provider);
    }

    public List<Service> findByDomain(ServiceDomain domain) {
        return serviceRepository.findByDomain(domain);
    }

    public List<Service> findBySubdomain(ServiceSubdomain subdomain) {
        return serviceRepository.findBySubdomain(subdomain);
    }

    public List<Service> findByRegion(Region region) {
        return serviceRepository.findByRegion(region);
    }

    public List<Service> findByPriceRange(double start, double end) {
        return serviceRepository.findByPriceBetween(start, end);
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    //TODO implementare exceptii
}
