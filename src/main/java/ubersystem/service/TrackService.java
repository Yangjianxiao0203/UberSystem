package ubersystem.service;

import org.springframework.transaction.annotation.Transactional;
import ubersystem.pojo.Track;

public interface TrackService {
    @Transactional
    public Track createTrack(Track track);

    @Transactional
    public void listenToTrack(String channelName);
}
