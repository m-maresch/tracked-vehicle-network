package at.htl.vehiclemanagementservice.service.web;

import at.htl.vehiclemanagementservice.domain.VehicleRepository;
import at.htl.vehiclemanagementservice.dto.vehicle.SimplifiedVehicle;
import at.htl.vehiclemanagementservice.dto.vehicleAction.SetLimitRequest;
import at.htl.vehiclemanagementservice.dto.vehicleAction.VehicleMovementRequest;
import at.htl.vehiclemanagementservice.handler.MqttHandler;
import at.htl.vehiclemanagementservice.model.Vehicle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;

@Service
public class VehicleActionManagerService {

    private final MqttHandler mqttHandler;

    @Autowired
    public VehicleActionManagerService(MqttHandler mqttHandler) {
        this.mqttHandler = mqttHandler;
    }

    public Mono<Void> setTemperatureLimit(SetLimitRequest setLimitRequest) {
        return publishSetLimitCommand("setTemperatureLimitCommand", setLimitRequest);
    }

    public Mono<Void> setHumidityLimit(SetLimitRequest setLimitRequest) {
        return publishSetLimitCommand("setHumidityLimitCommand", setLimitRequest);
    }

    public Mono<Void> setLightIntensityLimit(SetLimitRequest setLimitRequest) {
        return publishSetLimitCommand("setLightIntensityLimitCommand", setLimitRequest);
    }

    public Mono<Void> rotateVehicleLeft(VehicleMovementRequest vehicleMovementRequest) {
        return publishVehicleMovementCommand("rotateVehicleLeftCommand", vehicleMovementRequest);
    }

    public Mono<Void> rotateVehicleRight(VehicleMovementRequest vehicleMovementRequest) {
        return publishVehicleMovementCommand("rotateVehicleRightCommand", vehicleMovementRequest);
    }

    public Mono<Void> moveVehicleForward(VehicleMovementRequest vehicleMovementRequest) {
        return publishVehicleMovementCommand("moveVehicleForwardCommand", vehicleMovementRequest);
    }

    private Mono<Void> publishSetLimitCommand(String type, SetLimitRequest setLimitRequest) {
        var node = setupNode(type, setLimitRequest.getId());
        ((ObjectNode) node).put("limit", setLimitRequest.getLimit());
        mqttHandler.publish(node);
        return Mono.empty();
    }

    private Mono<Void> publishVehicleMovementCommand(String type, VehicleMovementRequest vehicleMovementRequest) {
        var node = setupNode(type, vehicleMovementRequest.getId());
        ((ObjectNode) node).put("value",vehicleMovementRequest.getValue());
        mqttHandler.publish(node);
        return Mono.empty();
    }

    private JsonNode setupNode(String type, Long id) {
        var mapper = new ObjectMapper();
        var node = (JsonNode)mapper.createObjectNode();
        ((ObjectNode) node).put("type", type);
        ((ObjectNode) node).put("id", id);
        return node;
    }
}
