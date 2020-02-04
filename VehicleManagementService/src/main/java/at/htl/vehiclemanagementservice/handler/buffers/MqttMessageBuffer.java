package at.htl.vehiclemanagementservice.handler.buffers;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageBuffer extends Buffer<JsonNode> {

    @Autowired
    public MqttMessageBuffer(Ignite ignite) {
        super(5000, "mqttMessageQueue", ignite);
    }
}
