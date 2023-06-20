package ubersystem.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

public interface MessageHandler {
    void handle(String topic, MqttMessage message);
}
