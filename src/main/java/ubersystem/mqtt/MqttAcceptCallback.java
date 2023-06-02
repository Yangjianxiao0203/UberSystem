package ubersystem.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttAcceptCallback implements MqttCallbackExtended {

    @Autowired
    @Lazy
    MqttAcceptClient mqttAcceptClient;

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("MQTT Accept Client connection completed: {}", s);
        log.info("client: {}", MqttAcceptClient.getClient().getClientId());
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTT connection lost: {}", throwable.getMessage());
        log.info("reconnecting...");
        if(MqttAcceptClient.client==null) {
            mqttAcceptClient.reconnect();
        }
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("MQTT message received: {}", mqttMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("MQTT delivery complete: {}", iMqttDeliveryToken);
    }
}
