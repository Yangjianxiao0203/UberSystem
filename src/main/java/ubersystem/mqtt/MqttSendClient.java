package ubersystem.mqtt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class MqttSendClient {
    public static MqttClient client;

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MqttSendClient.client = client;
    }

    @Autowired
    MqttConfiguration mqttConfiguration;

    @Autowired
    MqttSendCallback mqttSendCallback;

    public void connect() {
        MqttClient client;
        try {
            client =new MqttClient(mqttConfiguration.getHost(), mqttConfiguration.getSendClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(mqttConfiguration.isCleanSession());
            options.setUserName(mqttConfiguration.getUserName());
            options.setPassword(mqttConfiguration.getPassword().toCharArray());
            options.setConnectionTimeout(mqttConfiguration.getTimeout());
            options.setKeepAliveInterval(mqttConfiguration.getKeepAlive());
            try {
                client.setCallback(mqttSendCallback);
                client.connect(options);
                MqttSendClient.setClient(client);
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
        log.info("MQTT send client subscribe topic: {}", mqttConfiguration.getTopic()[0]);
        try {
            client.subscribe(mqttConfiguration.getTopic()[0], mqttConfiguration.getQos());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void unsubscribe() {
        log.info("MQTT send client unsubscribe topic: {}", mqttConfiguration.getTopic()[0]);
        try {
            client.unsubscribe(mqttConfiguration.getTopic()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
