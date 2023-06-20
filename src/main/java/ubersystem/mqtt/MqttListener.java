package ubersystem.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ubersystem.mqtt.Ride.RideClient;
import ubersystem.mqtt.Track.TrackClient;

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

    @Autowired
    private TrackClient trackClient;
    @Bean
    public TrackClient getTrackClient() {
        trackClient.connect();
        trackClient.subscribe();
        return trackClient;
    }
}
