package ubersystem.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ubersystem.mqtt.Ride.RideClient;

@Configuration
public class MqttListener {

    @Autowired
    private RideClient rideClient;

    @Bean
    public RideClient getRideClient() {
        rideClient.connect();
        rideClient.subscribe();
        return rideClient;
    }
}
