package at.htl.vehiclemanagementservice.handler.buffers;

import at.htl.vehiclemanagementservice.dto.vehicle.EnvironmentalDataMeasuredEvent;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TemperatureMeasuredEventBuffer extends Buffer<EnvironmentalDataMeasuredEvent> {

    @Autowired
    public TemperatureMeasuredEventBuffer(Ignite ignite) {
        super(1000, "temperatureMeasuredEventQueue", ignite);
    }
}
