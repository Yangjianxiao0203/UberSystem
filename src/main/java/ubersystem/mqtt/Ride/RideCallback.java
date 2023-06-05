package ubersystem.mqtt.Ride;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RideCallback implements MqttCallbackExtended {

    @Autowired
    @Lazy
    RideClient rideClient;

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("Ride Client connection completed: {}", s);
        log.info("client: {}", RideClient.getClient().getClientId());
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("Ride connection lost: {}", throwable.getMessage());

        log.info("reconnecting...");
        if(RideClient.client==null) {
            rideClient.reconnect();
        }
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("Ride message received: {}", mqttMessage);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("MQTT delivery complete: {}", iMqttDeliveryToken);
    }
}
