package ubersystem.mqtt.Track;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
import ubersystem.mqtt.MessageHandler;

@Slf4j
@Component
public class TrackMessageHandler implements MessageHandler {
    @Override
    public void handle(String topic, MqttMessage message) {
        log.info("Track message received: {}", message);
    }
}
