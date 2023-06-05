package ubersystem.mqtt.Ride;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class RideClient {
    public static MqttClient client;

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        RideClient.client = client;
    }

    @Autowired
    private RideConfig rideConfig;

    @Autowired
    private RideCallback rideCallback;

    public void connect() {
        MqttClient client;
        try {
            client =new MqttClient(rideConfig.getHost(), rideConfig.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(rideConfig.isCleanSession());
            options.setUserName(rideConfig.getUserName());
            options.setPassword(rideConfig.getPassword().toCharArray());
            options.setConnectionTimeout(rideConfig.getTimeout());
            options.setKeepAliveInterval(rideConfig.getKeepAlive());
            try {
                client.setCallback(rideCallback);
                client.connect(options);
                RideClient.setClient(client);
                log.info("Ride Connection established");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
        log.info("ride client start to subscribe topics");
        try {
            int qos = rideConfig.getQos();
            for(String channel: rideConfig.getChannels()) {
                client.subscribe(channel, qos);
                log.info("subscribed to topic: " + channel);
            }
            log.info("ride client subscribed process finished");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        try {
            client.reconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void unsubscribe() {
        log.info("Ride client start to unsubscribe from topics");
        try {
            for (String channel : rideConfig.getChannels()) {
                if(client.getTopic(channel)==null) {continue;}
                client.unsubscribe(channel);
                log.info("Unsubscribed from topic: " + channel);
            }
            log.info("Ride client unsubscribe process finished");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
