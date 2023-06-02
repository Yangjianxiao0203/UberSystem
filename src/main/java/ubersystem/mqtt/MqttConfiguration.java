package ubersystem.mqtt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description: MQTT 配置类
 */

@Component
@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Slf4j
@Data
public class MqttConfiguration {

    private String host;
    private String userName;
    private String password;
    private String clientId;
    private String defaultTopic;
    private String[] topics;
    private int timeout;
    private int keepAlive;

    private MqttClient client;

    @Bean
    public MqttClient getClient() {
        if (client == null) {
            connect();
        }
        return client;
    }

    public void connect() {
        try {
            client = new MqttClient(host, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepAlive);
            try {
                client.setCallback(new PushCallback());
                IMqttToken iMqttToken = client.connectWithResult(options);
                boolean complete = iMqttToken.isComplete();
                log.info("MQTT连接"+(complete?"成功":"失败"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
