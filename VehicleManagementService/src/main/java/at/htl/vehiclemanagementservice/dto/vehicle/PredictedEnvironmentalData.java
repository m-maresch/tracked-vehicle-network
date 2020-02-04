package at.htl.vehiclemanagementservice.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PredictedEnvironmentalData implements Serializable {

    private Double temperature;

    private Double humidity;

    private Double lightIntensity;
}
