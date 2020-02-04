package at.htl.vehiclemanagementservice.service;

import at.htl.vehiclemanagementservice.domain.VehicleRepository;
import at.htl.vehiclemanagementservice.handler.MqttHandler;
import at.htl.vehiclemanagementservice.handler.buffers.PingResponseBuffer;
import at.htl.vehiclemanagementservice.model.Vehicle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PingService {

    private final Ignite ignite;
    private final PingResponseBuffer buffer;
    private final MqttHandler mqttHandler;
    private final VehicleRepository repository;

    @Autowired
    public PingService(Ignite ignite, MqttHandler mqttHandler, PingResponseBuffer buffer, VehicleRepository repository) {
        this.ignite = ignite;
        this.mqttHandler = mqttHandler;
        this.buffer = buffer;
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 2000)
    public void sendPingRequestForEach() {
        repository.findAll().forEach(v -> sendPingRequest(v.getId()));
    }

    public void sendPingRequest(Long vehicleId) {
        var cache = ignite.<Long, Long>cache("pingRequestCache");
        var containsKey = cache.containsKeyAsync(vehicleId);

        var mapper = new ObjectMapper();

        var node = (JsonNode)mapper.createObjectNode();
        ((ObjectNode) node).put("type", "pingRequest");
        ((ObjectNode) node).put("id", vehicleId);

        mqttHandler.publish(node);

        if (!containsKey.get()) {
            cache.putAsync(vehicleId, 0L);
            return;
        }

        cache.getAsync(vehicleId).listen(f -> {
            var a = f.get();
            Long count = a + 1;
            if (count < 10) {
                cache.putAsync(vehicleId, count);
            } else {
                ignite.<Long, Vehicle>cache("vehicleCache").removeAsync(vehicleId);
                cache.removeAsync(vehicleId);
            }

        });
    }

    @Scheduled(fixedDelay = 50)
    public void handlePingResponse() {
        var response = buffer.take();

        if (response != null) {
            var cache = ignite.<Long, Long>cache("pingRequestCache");
            cache.removeAsync(response.getId());
        }
    }
}
