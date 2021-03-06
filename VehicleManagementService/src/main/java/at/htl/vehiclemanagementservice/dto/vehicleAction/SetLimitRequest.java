package at.htl.vehiclemanagementservice.dto.vehicleAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SetLimitRequest implements Serializable {

    private Long id;

    private Integer limit;
}
