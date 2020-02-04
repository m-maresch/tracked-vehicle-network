package at.htl.vehiclemanagementservice.handler;

import at.htl.vehiclemanagementservice.handler.buffers.MqttMessageBuffer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Queue;

@Slf4j
@Component
public class MqttHandler implements MqttCallback {

    @Value("${mqtt.vernemq.brokers}")
    private String broker;
    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.in.topic.name}")
    private String inTopic;
    @Value("${mqtt.out.topic.name}")
    private String outTopic;
    private int qos = 2;
    private MqttClient mqttClient;

    private final MqttMessageBuffer buffer;

    @Autowired
    public MqttHandler(MqttMessageBuffer buffer) {
        this.buffer = buffer;
    }

    @PostConstruct
    private void start() {
        try {
            var persistence = new MemoryPersistence();

            mqttClient = new MqttClient(broker, clientId, persistence);
            var connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            mqttClient.setCallback(this);
            mqttClient.subscribe(inTopic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }

    public void publish(JsonNode node) {
        try {
            var message = new MqttMessage(node.toString().getBytes());
            message.setQos(qos);
            mqttClient.publish(outTopic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        var mapper = new ObjectMapper();

        try {
            var node = mapper.readTree(new String(message.getPayload()));
            buffer.buffer(node);
        } catch (IOException ex) {
            log.warn(ex.toString());
        }
    }

    @PreDestroy
    private void stop() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}

    public void connectionLost(Throwable throwable) {}
}
