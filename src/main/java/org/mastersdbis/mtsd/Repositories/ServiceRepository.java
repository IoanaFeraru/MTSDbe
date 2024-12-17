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

    List<Service> findByProvider(Provider provider);

    List<Service> findByDomain(ServiceDomain domain);

    List<Service> findBySubdomain(ServiceSubdomain subdomain);

    List<Service> findByRegion(Region region);

    List<Service> findByPriceBetween(double start, double end);

    @Query("SELECT s FROM Service s WHERE " +
            "(s.provider = :provider OR :provider IS NULL) AND " +
            "(s.domain = :domain OR :domain IS NULL) AND " +
            "(s.subdomain = :subdomain OR :subdomain IS NULL) AND " +
            "(s.region = :region OR :region IS NULL) AND " +
            "(s.price BETWEEN :start AND :end OR :start IS NULL OR :end IS NULL)")
    List<Service> searchServices(Provider provider, ServiceDomain domain, ServiceSubdomain subdomain, Region region, Double start, Double end);
}
