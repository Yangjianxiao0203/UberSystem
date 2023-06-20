package ubersystem.mqtt.Track;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Slf4j
public class TrackConfig {
    private String host="tcp://localhost:1883";
    private String clientId="track-backend";
    private String userName="Track";
    private String password="distinctive0930";
    private int timeout=3000;
    private int keepAlive=60;
    private Integer qos=1;
    private List<String> channels;
    private boolean cleanSession=true;

    @PostConstruct
    public void setChannel() {
        if(channels == null) {
            channels = setChannelsByTxT("src/main/java/ubersystem/mqtt/Ride/StateAndCityInAmerican.txt");
        }
        log.info("MQTT configuration completed: {}", this);
        log.info("MQTT channels completed with size: {}", channels.size());
    }

    public List<String> setChannelsByTxT(String file){
        List<String> channels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String state = parts[0].trim();
                    String city = parts[1].trim();
                    channels.add("track-" + state + "-" + city);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channels;
    }
}
