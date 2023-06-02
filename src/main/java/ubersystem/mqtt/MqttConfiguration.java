package ubersystem.mqtt;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqtt")
@Data
@Slf4j
public class MqttConfiguration {
    private String host;
    private String sendClientId;
    private String acceptClientId;
    private String userName;
    private String password;
    private int timeout;
    private int keepAlive;
    private Integer qos;
    private String[] topic;
    private boolean cleanSession;

    @PostConstruct
    public void afterPropertiesSet() {
        log.info("MQTT configuration completed: {}", this);
    }
}
