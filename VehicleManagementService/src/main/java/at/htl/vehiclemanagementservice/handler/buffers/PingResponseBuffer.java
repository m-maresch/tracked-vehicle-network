package at.htl.vehiclemanagementservice.handler.buffers;

import at.htl.vehiclemanagementservice.dto.vehicle.PingResponse;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PingResponseBuffer extends Buffer<PingResponse> {

    @Autowired
    public PingResponseBuffer(Ignite ignite) {
        super(5000, "pingResponseQueue", ignite);
    }
}
