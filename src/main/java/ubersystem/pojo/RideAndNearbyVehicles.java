package ubersystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideAndNearbyVehicles {
    Ride ride;
    List<String> nearbyVehicles;
}
