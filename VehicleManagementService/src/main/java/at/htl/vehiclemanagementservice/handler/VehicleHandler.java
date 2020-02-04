package at.htl.vehiclemanagementservice.handler;

import at.htl.vehiclemanagementservice.dto.Gps;
import at.htl.vehiclemanagementservice.dto.vehicle.EnvironmentalData;
import at.htl.vehiclemanagementservice.dto.vehicle.GetAllVehiclesResponse;
import at.htl.vehiclemanagementservice.dto.vehicle.PredictedEnvironmentalData;
import at.htl.vehiclemanagementservice.dto.vehicle.SimplifiedVehicle;
import at.htl.vehiclemanagementservice.service.web.VehicleManagerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class VehicleHandler {

    private final VehicleManagerService vehicleManagerService;

    @Autowired
    public VehicleHandler(VehicleManagerService vehicleManagerService) {
        this.vehicleManagerService = vehicleManagerService;
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ok()
                .body(vehicleManagerService.getAll(), GetAllVehiclesResponse.class);
    }

    @NotNull
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return ok()
                .body(vehicleManagerService.get(Long.valueOf(serverRequest.pathVariable("id"))), SimplifiedVehicle.class);
    }

    @NotNull
    public Mono<ServerResponse> getGps(ServerRequest serverRequest) {
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

        var dt = serverRequest.queryParam("datetime")
                .map(s -> LocalDateTime.parse(s, formatter))
                .orElse(LocalDateTime.now());

        return ok()
                .body(vehicleManagerService.getGps(Long.valueOf(serverRequest.pathVariable("id")), dt), Gps.class);
    }

    @NotNull
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SimplifiedVehicle.class)
                .map(simplifiedVehicle -> vehicleManagerService.update(Long.valueOf(serverRequest.pathVariable("id")), simplifiedVehicle))
                .then(ok().build());
    }

    @NotNull
    public Mono<ServerResponse> getEnvironmentalData(ServerRequest serverRequest) {
        var days = serverRequest.queryParam("days")
                .map(Integer::parseInt)
                .orElse(-1);

        if (days == -1) {
            return ok()
                    .body(vehicleManagerService.getEnvironmentalData(Long.valueOf(serverRequest.pathVariable("id"))), EnvironmentalData.class);
        }

        return ok()
                .body(vehicleManagerService.getEnvironmentalData(Long.valueOf(serverRequest.pathVariable("id")), days), EnvironmentalData.class);
    }

    @NotNull
    public Mono<ServerResponse> getPredictedEnvironmentalData(ServerRequest serverRequest) {
        var days = serverRequest.queryParam("days")
                .map(Integer::parseInt)
                .orElse(7);

        if (days > 7) days = 7;

        return ok()
                .body(vehicleManagerService.getPredictedEnvironmentalData(Long.valueOf(serverRequest.pathVariable("id")), days), PredictedEnvironmentalData.class);
    }
}
