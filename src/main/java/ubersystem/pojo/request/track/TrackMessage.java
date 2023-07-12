package ubersystem.pojo.request.track;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ubersystem.Enums.RideStatus;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class TrackMessage {
    @JsonProperty("user")
    private String user;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lng")
    private double longitude;

    @JsonProperty("action")
    private String action;

    @JsonProperty("rid")
    private Long rid;

    @JsonProperty("speed")
    private double speed;
}
