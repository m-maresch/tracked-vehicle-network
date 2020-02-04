package at.htl.vehiclemanagementservice.configuration;

import at.htl.vehiclemanagementservice.domain.RawEnvironmentalDataAggregationsStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding({RawEnvironmentalDataAggregationsStream.class})
public class StreamConfig {
}
