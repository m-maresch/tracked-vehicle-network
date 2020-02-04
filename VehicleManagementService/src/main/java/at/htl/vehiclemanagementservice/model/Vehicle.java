package at.htl.vehiclemanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Vehicle implements Serializable {

    @QuerySqlField(index = true)
    @NotNull
    private Long id;

    @QuerySqlField(index = true)
    @NotBlank
    private String name;
}
