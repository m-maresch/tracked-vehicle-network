package at.htl.vehiclemanagementservice.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllVehiclesResponse {
    private List<SimplifiedVehicle> vehicles;
}
