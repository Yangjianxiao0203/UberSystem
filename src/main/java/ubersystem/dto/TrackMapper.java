package ubersystem.dto;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Track;

public interface TrackMapper {
    @Insert("INSERT INTO track(ride_id, time_sequence, coordinate_id, speed_track, altitude) VALUES (#{track.rideId}, #{track.timeSequence}, #{track.coordinateId}, #{track.speedTrack}, #{track.altitude})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertTrack(@Param("track") Track track);
}
