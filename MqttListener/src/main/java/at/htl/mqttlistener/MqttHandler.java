package at.htl.mqttlistener;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Component
public class MqttHandler implements MqttCallback {

    @Value("${mqtt.vernemq.brokers}")
    private String broker;
    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.topic.name}")
    private String topic;
    private MqttClient mqttClient;

    @PostConstruct
    private void start() {
        try {
            var persistence = new MemoryPersistence();

            mqttClient = new MqttClient(broker, clientId, persistence);
            var connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            mqttClient.setCallback(this);
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.info(message.toString());
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
