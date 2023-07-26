package ubersystem.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.RideStatus;
import ubersystem.mapper.CoordinateMapper;
import ubersystem.mapper.TrackMapper;
import ubersystem.mqtt.Track.TrackClient;
import ubersystem.pojo.Ride;
import ubersystem.pojo.Track;
import ubersystem.pojo.request.track.TrackMessage;
import ubersystem.service.MqttService;
import ubersystem.service.RideService;
import ubersystem.service.TrackService;

import java.time.LocalDateTime;

@Service
@Slf4j
@Data
public class TrackServiceImpl extends MqttService implements TrackService {

    @Autowired
    TrackMapper trackMapper;

    @Autowired
    TrackClient trackClient;

    @Autowired
    CoordinateMapper coordinateMapper;

    @Autowired
    RideService rideService;

    @Override
    public Track createTrack(Track track) {
        int res = trackMapper.insertTrack(track);
        return track;
    }

    @Override
    public Track getTrackByRid(Long rid) {
        return trackMapper.selectTrackByRideId(rid);
    }


    @Override
    public void listenToTrack(String channelName) {
        trackClient.subscribe(channelName);
    }

    @Override
    public <T> void publish(T message, String channel) {
        MqttMessage mqttMessage = getJson(message);
        try {
            TrackClient.getClient().publish(channel, mqttMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleAction(TrackMessage trackMessage) {
        if(trackMessage.getUser()==null) {
            log.info("this track has been cancelled");
            deleteTrackByRid(trackMessage.getRid());
            return;
        }
        if(!trackMessage.getAction().equals("")) {
            Track track= trackMapper.selectTrackByRideId(trackMessage.getRid());
            Ride ride=rideService.getRideByRid(trackMessage.getRid());
            if(track!=null) {
                track.setTimeSequence(LocalDateTime.now());
                track.setCoordinate(trackMessage.getLatitude()+","+trackMessage.getLongitude());
                track.setSpeedTrack(trackMessage.getSpeed());
                trackMapper.updateTrack(track);
            }
            if(ride!=null) {
                String curStatus= trackMessage.getAction();
                RideStatus rideStatus = RideStatus.valueOf(curStatus);
                setStatus(ride, rideStatus);
                rideService.updateRide(ride);
            }
            log.info("Track message saved, track: {}, ride status: {}", track,ride.getStatus());
        }
    }

    @Override
    public void deleteTrackByRid(Long rid) {
        trackMapper.deleteTrackByRid(rid);
    }

    public void setStatus(Ride ride, RideStatus status) {
        if(status == RideStatus.Arrived) {
            ride.setArrivalTime(LocalDateTime.now());
        }
        else if(status == RideStatus.Cancelled) {
            ride.setCancellationTime(LocalDateTime.now());
        }
        else if(status == RideStatus.PickedUpPassenger) {
            ride.setPickUpTime(LocalDateTime.now());
        }
        else if(status == RideStatus.Created) {
            ride.setCreationTime(LocalDateTime.now());
        }
        else if(status==RideStatus.OnRide) {
            ride.setStatus(RideStatus.OnRide);
        }
        ride.setStatus(status);
    }
}
