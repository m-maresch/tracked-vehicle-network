package at.htl.vehiclemanagementservice.configuration;

import at.htl.vehiclemanagementservice.model.AnalysationResult;
import at.htl.vehiclemanagementservice.model.Vehicle;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@EnableIgniteRepositories(basePackages = "at.htl.vehiclemanagementservice")
public class IgniteConfig {

    @Bean
    public Ignite igniteInstance() {
        var config = new IgniteConfiguration();
        var dataStorageConfiguration = new DataStorageConfiguration();
        var dataRegionConfiguration = new DataRegionConfiguration();
        dataRegionConfiguration.setPersistenceEnabled(true);
        dataStorageConfiguration.setDefaultDataRegionConfiguration(dataRegionConfiguration);
        config.setDataStorageConfiguration(dataStorageConfiguration);
        var vehicleCache = new CacheConfiguration<Long, Vehicle>("vehicleCache");
        var analysationResultCache = new CacheConfiguration<UUID, AnalysationResult>("analysationResultCache");
        var pingRequestCache = new CacheConfiguration<Long, Long>("pingRequestCache");
        vehicleCache.setIndexedTypes(Long.class, Vehicle.class);
        analysationResultCache.setIndexedTypes(UUID.class, AnalysationResult.class);
        pingRequestCache.setIndexedTypes(Long.class, Long.class);
        config.setCacheConfiguration(vehicleCache, analysationResultCache, pingRequestCache);
        var ignite = Ignition.start(config);
        ignite.cluster().active(true);
        return ignite;
    }
}
