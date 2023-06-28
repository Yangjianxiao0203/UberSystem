package ubersystem.mqtt.Track;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ubersystem.mapper.TrackMapper;
import ubersystem.mqtt.MessageHandler;
import ubersystem.pojo.Track;
import ubersystem.utils.ChannelGenerator;

@Slf4j
@Component
public class TrackMessageHandler implements MessageHandler {
    @Autowired
    TrackMapper trackMapper;
    @Override
    public void handle(String topic, MqttMessage message) {
        Long id = Long.parseLong(ChannelGenerator.decodeTrackChannelName(topic));
        //update track
        Track track = trackMapper.selectTrackByRideId(id);
        if (track == null) {
            track = new Track();
            track.setRideId(id);
            track.setMqttChannelName(topic);
            trackMapper.insertTrack(track);
        } else {
            // todo: 根据你的业务逻辑更新track的其他信息

            trackMapper.updateTrack(track);
        }
    }
}
