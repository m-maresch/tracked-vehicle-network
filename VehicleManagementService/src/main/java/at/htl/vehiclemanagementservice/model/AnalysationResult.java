package at.htl.vehiclemanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AnalysationResult implements Serializable {

    @QuerySqlField(index = true)
    @NotNull
    private UUID id;

    @QuerySqlField(index = true)
    @NotNull
    private Long vehicleId;

    private Vehicle vehicle;

    @QuerySqlField(index = true)
    @NotNull
    private Double temperature;

    @QuerySqlField(index = true)
    @NotNull
    private Double humidity;

    @QuerySqlField(index = true)
    @NotNull
    private Double lightIntensity;

    @QuerySqlField(index = true)
    @NotNull
    private Double latitude;

    @QuerySqlField(index = true)
    @NotNull
    private Double longitude;

    @QuerySqlField(index = true)
    @NotNull
    private LocalDateTime measuredAt;
}
