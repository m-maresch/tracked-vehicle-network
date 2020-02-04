package at.htl.vehiclemanagementservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Gps {

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("long")
    private Double longitude;
}
