package ubersystem.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttListener {
    @Autowired
    private MqttAcceptClient mqttAcceptClient;

    @Autowired
    private MqttSendClient mqttSendClient;

    @Bean
    public MqttAcceptClient getMqttAcceptClient() {
        mqttAcceptClient.connect();
        mqttAcceptClient.subscribe();
        return mqttAcceptClient;
    }

    @Bean
    public MqttSendClient getMqttSendClient() {
        mqttSendClient.connect();
        mqttSendClient.connect();
        return mqttSendClient;
    }
}
