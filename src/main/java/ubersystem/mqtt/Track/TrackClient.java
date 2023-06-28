package ubersystem.mqtt.Track;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ubersystem.mqtt.Ride.RideClient;

@Component
@Data
@Slf4j
public class TrackClient {
    public static MqttClient client;
    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        TrackClient.client = client;
    }

    @Autowired
    private TrackConfig trackConfig;

    @Autowired
    private TrackCallback trackCallback;

    public void connect() {
        MqttClient client;
        try {
            client =new MqttClient(trackConfig.getHost(), trackConfig.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(trackConfig.isCleanSession());
            options.setUserName(trackConfig.getUserName());
            options.setPassword(trackConfig.getPassword().toCharArray());
            options.setConnectionTimeout(trackConfig.getTimeout());
            options.setKeepAliveInterval(trackConfig.getKeepAlive());
            try {
                client.setCallback(trackCallback);
                client.connect(options);
                TrackClient.setClient(client);
                log.info("Track Connection established");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
        log.info("track client start to subscribe topics");
        try {
            int qos = trackConfig.getQos();
            for(String channel: trackConfig.getChannels()) {
                client.subscribe(channel, qos);
            }
            log.info("track client subscribe topics success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String channelName) {
        log.info("track client start to subscribe topic:" + channelName);
        try {
            int qos=trackConfig.getQos();
            client.subscribe(channelName, qos);
        } catch (Exception e) {
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
        try {
            for(String channel: trackConfig.getChannels()) {
                client.unsubscribe(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String messageContent) {
        try {
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(trackConfig.getQos());
            client.publish(topic, message);
            log.info("Published message to topic: {}", topic);
        } catch (MqttException e) {
            log.error("Error publishing message to MQTT broker", e);
        }
    }
}
