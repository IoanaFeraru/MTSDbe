package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Service.Region;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.Service.ServiceSubdomain;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByProvider(Provider provider);

    List<Service> findByDomain(ServiceDomain domain);

    List<Service> findBySubdomain(ServiceSubdomain subdomain);

    List<Service> findByRegion(Region region);

    List<Service> findByPriceBetween(double start, double end);
}
