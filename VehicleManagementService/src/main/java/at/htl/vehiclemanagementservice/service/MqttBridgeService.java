package at.htl.vehiclemanagementservice.service;

import at.htl.vehiclemanagementservice.dto.vehicle.EnvironmentalDataMeasuredEvent;
import at.htl.vehiclemanagementservice.dto.vehicle.GpsDataMeasuredEvent;
import at.htl.vehiclemanagementservice.dto.vehicle.PingResponse;
import at.htl.vehiclemanagementservice.dto.vehicle.RegisterVehicleRequest;
import at.htl.vehiclemanagementservice.handler.buffers.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class MqttBridgeService {

    private final RegisterVehicleRequestBuffer registerVehicleRequestBuffer;

    private final PingResponseBuffer pingResponseBuffer;

    private final TemperatureMeasuredEventBuffer temperatureMeasuredEventBuffer;

    private final HumidityMeasuredEventBuffer humidityMeasuredEventBuffer;

    private final LightIntensityMeasuredEventBuffer lightIntensityMeasuredEventBuffer;

    private final GpsDataMeasuredEventBuffer gpsDataMeasuredEventBuffer;

    private final MqttMessageBuffer mqttMessageBuffer;

    @Autowired
    public MqttBridgeService(MqttMessageBuffer mqttMessageBuffer, GpsDataMeasuredEventBuffer gpsDataMeasuredEventBuffer, HumidityMeasuredEventBuffer humidityMeasuredEventBuffer, LightIntensityMeasuredEventBuffer lightIntensityMeasuredEventBuffer, PingResponseBuffer pingResponseBuffer, RegisterVehicleRequestBuffer registerVehicleRequestBuffer, TemperatureMeasuredEventBuffer temperatureMeasuredEventBuffer) {
        this.mqttMessageBuffer = mqttMessageBuffer;
        this.gpsDataMeasuredEventBuffer = gpsDataMeasuredEventBuffer;
        this.humidityMeasuredEventBuffer = humidityMeasuredEventBuffer;
        this.lightIntensityMeasuredEventBuffer = lightIntensityMeasuredEventBuffer;
        this.pingResponseBuffer = pingResponseBuffer;
        this.registerVehicleRequestBuffer = registerVehicleRequestBuffer;
        this.temperatureMeasuredEventBuffer = temperatureMeasuredEventBuffer;
    }

    @Scheduled(fixedDelay = 50)
    public void runMqttMessageHandler() {
        handleMqttMessages()
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    public Flux<JsonNode> handleMqttMessages() {
        return Flux.<JsonNode>generate(synchronousSink -> {
                    JsonNode node = mqttMessageBuffer.take();

                    if (node != null) {
                        synchronousSink.next(node);
                    } else {
                        synchronousSink.next(new ObjectMapper().createObjectNode());
                    }
                })
                .doOnNext(mqttMessage -> {
                    if (!mqttMessage.has("type")) return;

                    final String request = "Request";
                    final String response = "Response";
                    final String vehicle = "Vehicle";
                    final String measured = "Measured";
                    final String event = "Event";

                    switch (mqttMessage.get("type").asText()) {
                        case "register" + vehicle + request:
                            registerVehicleRequestBuffer.buffer(new RegisterVehicleRequest(mqttMessage.get("id").asLong(), mqttMessage.get("name").asText()));
                            break;
                        case "ping" + response:
                            pingResponseBuffer.buffer(new PingResponse(mqttMessage.get("id").asLong()));
                            break;
                        case "temperature" + measured + event:
                            temperatureMeasuredEventBuffer.buffer(new EnvironmentalDataMeasuredEvent(mqttMessage.get("id").asLong(), mqttMessage.get("value").asDouble()));
                            break;
                        case "humidity" + measured + event:
                            humidityMeasuredEventBuffer.buffer(new EnvironmentalDataMeasuredEvent(mqttMessage.get("id").asLong(), mqttMessage.get("value").asDouble()));
                            break;
                        case "lightIntensity" + measured + event:
                            lightIntensityMeasuredEventBuffer.buffer(new EnvironmentalDataMeasuredEvent(mqttMessage.get("id").asLong(), mqttMessage.get("value").asDouble()));
                            break;
                        case "gpsData" + measured + event:
                            gpsDataMeasuredEventBuffer.buffer(new GpsDataMeasuredEvent(mqttMessage.get("id").asLong(), mqttMessage.get("latitude").asDouble(), mqttMessage.get("longitude").asDouble()));
                            break;
                    }
                });
    }
}
