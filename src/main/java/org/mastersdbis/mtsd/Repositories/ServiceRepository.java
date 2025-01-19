package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Service.Region;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.Service.ServiceSubdomain;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByProviderAndActiveTrue(Provider provider);

    List<Service> findByProvider(Provider provider);

    List<Service> findByDomainAndActiveTrue(ServiceDomain domain);

    List<Service> findBySubdomainAndActiveTrue(ServiceSubdomain subdomain);

    List<Service> findByRegionAndActiveTrue(Region region);

    @Query("SELECT s FROM Service s WHERE " +
            "s.active = true AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Service> searchServices(String searchTerm);

    List<Service> findByActiveTrue();
}
