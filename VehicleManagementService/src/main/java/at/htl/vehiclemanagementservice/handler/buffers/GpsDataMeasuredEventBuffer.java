package at.htl.vehiclemanagementservice.handler.buffers;

import at.htl.vehiclemanagementservice.dto.vehicle.GpsDataMeasuredEvent;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GpsDataMeasuredEventBuffer extends Buffer<GpsDataMeasuredEvent> {

    @Autowired
    public GpsDataMeasuredEventBuffer(Ignite ignite) {
        super(1000, "gpsDataMeasuredQueue", ignite);
    }
}
