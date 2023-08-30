package ubersystem.mqtt.Ride;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Slf4j
public class RideConfig {
    @Value("${SPRING_MQTT_HOST:ws://localhost:8083}")
    private String host;

    private String clientId="ride-backend";
    private String userName="Ride";
    private String password="distinctive0930";
    private int timeout=3000;
    private int keepAlive=60;
    private Integer qos=2;
    private List<String> channels;
    private boolean cleanSession=false;

//    @PostConstruct
//    public void setChannel() {
//        if(channels == null) {
//            channels = setChannelsByTxT("src/main/java/ubersystem/mqtt/Ride/StateAndCityInAmerican.txt");
//        }
//        log.info("MQTT configuration completed: {}", this);
//        log.info("MQTT channels completed with size: {}", channels.size());
//    }

    public List<String> setChannelsByTxT(String file){
        List<String> channels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String state = parts[0].trim();
                    String city = parts[1].trim();
                    channels.add("ride-" + state + "-" + city);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channels;
    }
}
