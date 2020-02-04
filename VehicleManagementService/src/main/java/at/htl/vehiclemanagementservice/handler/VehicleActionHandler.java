package at.htl.vehiclemanagementservice.handler;

import at.htl.vehiclemanagementservice.dto.vehicleAction.SetLimitRequest;
import at.htl.vehiclemanagementservice.dto.vehicleAction.VehicleMovementRequest;
import at.htl.vehiclemanagementservice.service.web.VehicleActionManagerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class VehicleActionHandler {

    private final VehicleActionManagerService vehicleActionManagerService;

    @Autowired
    public VehicleActionHandler(VehicleActionManagerService vehicleActionManagerService) {
        this.vehicleActionManagerService = vehicleActionManagerService;
    }

    @NotNull
    public Mono<ServerResponse> setTemperatureLimit(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SetLimitRequest.class)
                .map(vehicleActionManagerService::setTemperatureLimit)
                .then(ok().build());
    }

    @NotNull
    public Mono<ServerResponse> setHumidityLimit(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SetLimitRequest.class)
                .map(vehicleActionManagerService::setHumidityLimit)
                .then(ok().build());
    }

    @NotNull
    public Mono<ServerResponse> setLightIntensityLimit(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SetLimitRequest.class)
                .map(vehicleActionManagerService::setLightIntensityLimit)
                .then(ok().build());
    }

    @NotNull
    public Mono<ServerResponse> rotateVehicleLeft(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(VehicleMovementRequest.class)
                .map(vehicleActionManagerService::rotateVehicleLeft)
                .then(ok().build());
    }

    @NotNull
    public Mono<ServerResponse> rotateVehicleRight(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(VehicleMovementRequest.class)
                .map(vehicleActionManagerService::rotateVehicleRight)
                .then(ok().build());
    }

    @NotNull
    public Mono<ServerResponse> moveVehicleForward(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(VehicleMovementRequest.class)
                .map(vehicleActionManagerService::moveVehicleForward)
                .then(ok().build());
    }
}
