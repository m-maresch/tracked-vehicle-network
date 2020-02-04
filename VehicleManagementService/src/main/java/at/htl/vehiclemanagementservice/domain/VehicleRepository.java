package at.htl.vehiclemanagementservice.domain;

import at.htl.vehiclemanagementservice.model.Vehicle;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryConfig(cacheName = "vehicleCache")
public interface VehicleRepository extends IgniteRepository<Vehicle, Long> {
}
