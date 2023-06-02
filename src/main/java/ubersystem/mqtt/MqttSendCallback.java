package ubersystem.mqtt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class MqttSendCallback implements MqttCallbackExtended {

    @Autowired
    @Lazy
    MqttSendClient mqttSendClient;

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("MQTT Send Client connection completed: {}", s);
        log.info("client: {}", MqttSendClient.getClient().getClientId());
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTT connection lost: {}", throwable.getMessage());

        log.info("reconnecting...");
        if(MqttAcceptClient.client==null) {
            mqttSendClient.reconnect();
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
