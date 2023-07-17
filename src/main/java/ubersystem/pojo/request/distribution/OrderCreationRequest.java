package ubersystem.pojo.request.distribution;

import lombok.Data;
import ubersystem.Enums.RideType;

@Data
public class OrderCreationRequest {
    private Long uid;
    private Double pickUpLong;
    private Double pickUpLat;
    private String pickUpResolvedAddress;
    private String desLong;
    private String desLat;
    private String desResolvedAddress;
    private Double rideLength;
    private RideType type;
    private String province;
    private String city;
}
