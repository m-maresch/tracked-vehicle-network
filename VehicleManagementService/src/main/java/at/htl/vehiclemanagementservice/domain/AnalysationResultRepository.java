package at.htl.vehiclemanagementservice.domain;

import at.htl.vehiclemanagementservice.model.AnalysationResult;
import at.htl.vehiclemanagementservice.model.Vehicle;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RepositoryConfig(cacheName = "analysationResultCache")
public interface AnalysationResultRepository extends IgniteRepository<AnalysationResult, UUID> {
}
