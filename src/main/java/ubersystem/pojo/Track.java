package ubersystem.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    private Long id;
    private Long rideId;
    private LocalDateTime timeSequence;
    private Coordinate coordinate;
    private Double speedTrack;
    private Double altitude;
}
