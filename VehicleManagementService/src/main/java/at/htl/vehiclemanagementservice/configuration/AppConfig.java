package at.htl.vehiclemanagementservice.configuration;

import at.htl.vehiclemanagementservice.configuration.security.AllowAllSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AllowAllSecurityConfig.class, IgniteConfig.class, SchedulingConfig.class, StreamConfig.class})
public class AppConfig {
}
