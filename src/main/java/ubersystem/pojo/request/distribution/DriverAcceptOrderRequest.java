package ubersystem.pojo.request.distribution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverAcceptOrderRequest {
    private Long driverUid;
    private Double longitude;
    private Double latitude;
    private String numberPlate;
    private String vehicleInfo;
}
