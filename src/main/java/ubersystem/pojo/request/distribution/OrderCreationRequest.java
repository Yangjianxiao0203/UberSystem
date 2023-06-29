package ubersystem.pojo.request.distribution;

import lombok.Data;
import ubersystem.Enums.RideType;

@Data
public class OrderCreationRequest {
    private Long uid;
    private Double pickUpLong;
    private Double pickUpLat;
    private String pickUpResolvedAddress;
    private String desResolvedAddress;
    private RideType type;
    private String province;
    private String city;
}
