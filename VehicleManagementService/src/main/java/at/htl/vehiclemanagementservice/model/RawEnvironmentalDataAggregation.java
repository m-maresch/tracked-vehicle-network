package at.htl.vehiclemanagementservice.model;

import at.htl.vehiclemanagementservice.dto.Gps;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RawEnvironmentalDataAggregation implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Double temperature;

    @NotNull
    private Double humidity;

    @NotNull
    private Double lightIntensity;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private LocalDateTime measuredAt;
}
