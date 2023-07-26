package ubersystem.mqtt.Track;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ubersystem.Enums.RideStatus;
import ubersystem.mapper.RideMapper;
import ubersystem.mapper.TrackMapper;
import ubersystem.mqtt.MessageHandler;
import ubersystem.pojo.Ride;
import ubersystem.pojo.Track;
import ubersystem.pojo.request.track.TrackMessage;
import ubersystem.service.RideService;
import ubersystem.service.TrackService;
import ubersystem.utils.ChannelGenerator;

import java.time.LocalDateTime;

@Slf4j
@Component
public class TrackMessageHandler implements MessageHandler {
    @Autowired
    TrackMapper trackMapper;
    @Autowired
    RideService rideService;

    @Lazy
    @Autowired
    TrackService trackService;

    @Override
    public void handle(String topic, MqttMessage message) {

        ObjectMapper mapper = new ObjectMapper();
        TrackMessage trackMessage = null;
        try {
            trackMessage = mapper.readValue(message.toString(), TrackMessage.class);
            log.info("Track message received: {}", trackMessage);
            trackService.handleAction(trackMessage);
        } catch (Exception e) {
            //print out the message
            log.warn("mqtt message: {}",message);
            e.printStackTrace();
        }

    }
}
