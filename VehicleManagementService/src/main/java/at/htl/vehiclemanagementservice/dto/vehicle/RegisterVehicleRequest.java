package at.htl.vehiclemanagementservice.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterVehicleRequest {

    private Long id;

    private String name;
}
