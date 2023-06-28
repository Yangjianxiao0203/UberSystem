package ubersystem.mapper;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Track;

@Mapper
public interface TrackMapper {
    @Insert("INSERT INTO track(ride_id, time_sequence, coordinate, speed_track, altitude, mqtt_channel_name) VALUES (#{track.rideId}, #{track.timeSequence}, #{track.coordinate}, #{track.speedTrack}, #{track.altitude}, #{track.mqttChannelName})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertTrack(@Param("track") Track track);

    @Select("SELECT * FROM track WHERE ride_id = #{rideId}")
    Track selectTrackByRideId(@Param("rideId") Long rideId);

    @Update("UPDATE track SET ride_id = #{track.rideId}, time_sequence = #{track.timeSequence}, mqtt_channel_name = #{track.mqttChannelName}, coordinate = #{track.coordinate}, speed_track = #{track.speedTrack}, altitude = #{track.altitude} WHERE id = #{track.id}")
    int updateTrack(@Param("track") Track track);

}

