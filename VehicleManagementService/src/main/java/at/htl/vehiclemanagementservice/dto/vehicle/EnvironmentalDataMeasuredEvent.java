package at.htl.vehiclemanagementservice.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnvironmentalDataMeasuredEvent {

    private Long id;

    private Double value;
}
