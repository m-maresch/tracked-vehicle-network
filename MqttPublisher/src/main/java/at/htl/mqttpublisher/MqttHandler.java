package at.htl.mqttpublisher;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler implements MqttCallback {

    private String broker;
    private String clientId;
    private String topic;
    private int qos = 2;
    private MqttClient mqttClient;


    public MqttHandler(String broker, String clientId, String topic) {
        this.broker = broker;
        this.clientId = clientId;
        this.topic = topic;
    }

    public void start() {
        try {
            var persistence = new MemoryPersistence();

            mqttClient = new MqttClient(broker, clientId, persistence);
            var connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            mqttClient.setCallback(this);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }

    public void publish(String str) {
        try {
            var message = new MqttMessage(str.getBytes());
            message.setQos(qos);
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
    }

    public void stop() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}

    public void connectionLost(Throwable throwable) {}
}

