package at.htl.vehiclemanagementservice.configuration;

import at.htl.vehiclemanagementservice.service.DataAggregatorService;
import at.htl.vehiclemanagementservice.service.MqttBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.scheduler.Schedulers;

@Configuration
@EnableScheduling
public class SchedulingConfig {
}
