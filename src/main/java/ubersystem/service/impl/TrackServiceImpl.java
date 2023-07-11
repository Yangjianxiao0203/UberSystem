package ubersystem.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.mapper.CoordinateMapper;
import ubersystem.mapper.TrackMapper;
import ubersystem.mqtt.Track.TrackClient;
import ubersystem.pojo.Coordinate;
import ubersystem.pojo.Track;
import ubersystem.service.MqttService;
import ubersystem.service.TrackService;

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
    @Override
    public Track createTrack(Track track) {
        int res = trackMapper.insertTrack(track);
        MqttMessage message = getJson(track);
        try {
            TrackClient.getClient().publish(track.getMqttChannelName(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

}
