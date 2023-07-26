package ubersystem.service;

import org.springframework.transaction.annotation.Transactional;
import ubersystem.pojo.Track;
import ubersystem.pojo.request.track.TrackMessage;

public interface TrackService {
    @Transactional
    public Track createTrack(Track track);

    @Transactional
    public void listenToTrack(String channelName);

    @Transactional
    public Track getTrackByRid(Long rid);

    @Transactional
    public <T> void publish(T message, String channel);

    @Transactional
    public void handleAction(TrackMessage message);

    @Transactional
    public void deleteTrackByRid(Long rid);
}
