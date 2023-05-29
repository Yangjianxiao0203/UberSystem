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
@Entity
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ride_id")
    private Long rideId;
    @Column(name = "time_sequence")
    private LocalDateTime timeSequence;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinate coordinate;

    @Column(name="speed_track")
    private Double speedTrack;
    @Column(name="altitude")
    private Double altitude;
}
