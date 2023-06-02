package ubersystem.mqtt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class MqttAcceptClient {
    public static MqttClient client;

    public static MqttClient getClient() {
        return client;
    }
    public static void setClient(MqttClient client) {
        MqttAcceptClient.client = client;
    }

    @Autowired
    MqttConfiguration mqttConfiguration;

    @Autowired
    MqttAcceptCallback mqttAcceptCallback;

    public void connect() {
        MqttClient client;
        try {
            client =new MqttClient(mqttConfiguration.getHost(), mqttConfiguration.getAcceptClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(mqttConfiguration.isCleanSession());
            options.setUserName(mqttConfiguration.getUserName());
            options.setPassword(mqttConfiguration.getPassword().toCharArray());
            options.setConnectionTimeout(mqttConfiguration.getTimeout());
            options.setKeepAliveInterval(mqttConfiguration.getKeepAlive());
            try {
                client.setCallback(mqttAcceptCallback);
                client.connect(options);
                MqttAcceptClient.setClient(client);
                log.info("MQTT connection established");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void subscribe() {
        log.info("MQTT accept client subscribe topic: {}", mqttConfiguration.getTopic()[0]);
        try {
            client.subscribe(mqttConfiguration.getTopic()[0], mqttConfiguration.getQos());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void unsubscribe() {
        log.info("MQTT accept client unsubscribe topic: {}", mqttConfiguration.getTopic()[0]);
        try {
            client.unsubscribe(mqttConfiguration.getTopic()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
