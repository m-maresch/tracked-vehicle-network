package at.htl.vehiclemanagementservice.handler.buffers;

import at.htl.vehiclemanagementservice.dto.vehicle.RegisterVehicleRequest;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterVehicleRequestBuffer extends Buffer<RegisterVehicleRequest> {

    @Autowired
    public RegisterVehicleRequestBuffer(Ignite ignite) {
        super(1000, "registerVehicleRequestQueue", ignite);
    }
}
