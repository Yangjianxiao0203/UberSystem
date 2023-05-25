package ubersystem.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

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
