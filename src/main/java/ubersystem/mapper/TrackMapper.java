package ubersystem.mapper;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Track;

@Mapper
public interface TrackMapper {
    @Insert("INSERT INTO track(ride_id, time_sequence, coordinate, speed_track, altitude, mqtt_channel_name) VALUES (#{track.rideId}, #{track.timeSequence}, #{track.coordinate}, #{track.speedTrack}, #{track.altitude}, #{track.mqttChannelName})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertTrack(@Param("track") Track track);
}

