package ubersystem.mqtt.Track;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ubersystem.Enums.RideStatus;
import ubersystem.mapper.RideMapper;
import ubersystem.mapper.TrackMapper;
import ubersystem.mqtt.MessageHandler;
import ubersystem.pojo.Ride;
import ubersystem.pojo.Track;
import ubersystem.pojo.request.track.TrackMessage;
import ubersystem.utils.ChannelGenerator;

import java.time.LocalDateTime;

@Slf4j
@Component
public class TrackMessageHandler implements MessageHandler {
    @Autowired
    TrackMapper trackMapper;
    @Autowired
    RideMapper rideMapper;
    @Override
    public void handle(String topic, MqttMessage message) {

        ObjectMapper mapper = new ObjectMapper();
        TrackMessage trackMessage = null;
        try {
            trackMessage = mapper.readValue(message.toString(), TrackMessage.class);
            log.info("Track message received: {}", trackMessage);
            if(!trackMessage.getAction().equals("")) {
                Track track= trackMapper.selectTrackByRideId(trackMessage.getRid());
                Ride ride=rideMapper.getRideById(trackMessage.getRid());
                if(track!=null) {
                    track.setTimeSequence(LocalDateTime.now());
                    track.setCoordinate(trackMessage.getLatitude()+","+trackMessage.getLongitude());
                    trackMapper.updateTrack(track);
                }
                if(ride!=null) {
                    String curStatus= trackMessage.getAction();
                    RideStatus rideStatus = RideStatus.valueOf(curStatus);
                    ride.setStatus(rideStatus);
                    rideMapper.updateRide(ride);
                }
                log.info("Track message saved, track: {}, ride: {}", track,ride);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
