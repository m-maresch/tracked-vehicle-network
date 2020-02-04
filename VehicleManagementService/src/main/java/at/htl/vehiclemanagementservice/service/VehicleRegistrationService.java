package at.htl.vehiclemanagementservice.service;

import at.htl.vehiclemanagementservice.dto.vehicle.RegisterVehicleRequest;
import at.htl.vehiclemanagementservice.handler.buffers.RegisterVehicleRequestBuffer;
import at.htl.vehiclemanagementservice.model.Vehicle;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VehicleRegistrationService {

    private final Ignite ignite;
    private final RegisterVehicleRequestBuffer buffer;

    @Autowired
    public VehicleRegistrationService(Ignite ignite, RegisterVehicleRequestBuffer buffer) {
        this.ignite = ignite;
        this.buffer = buffer;
    }

    @Scheduled(fixedDelay = 50)
    public void handleVehicleRegistration() {
        RegisterVehicleRequest request = buffer.take();

        if (request != null) {
            ignite.<Long, Vehicle>cache("vehicleCache").putAsync(request.getId(), new Vehicle(request.getId(), request.getName()));
        }
    }
}
