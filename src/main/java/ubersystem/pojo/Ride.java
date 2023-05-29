package ubersystem.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ubersystem.Enums.RideStatus;
import ubersystem.Enums.RideType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    private Long id;
    private LocalDateTime creationTime;
    private Long passengerUid;
    private Long driverUid;
    private String mqttChannelName;
    private RideType rideType; // 'Economy', 'Comfort', 'Luxury'
    private String startPointCoordinates;
    private String startPointAddress;
    private String endPointCoordinates;
    private String endPointAddress;
    private RideStatus status; // 'Created', 'DriverAccepted', 'PickedUpPassenger', 'OnRide', 'Arrived', 'Cancelled', 'OnAlert'
    private LocalDateTime driverAcceptTime;
    private LocalDateTime pickUpTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime cancellationTime;
    private Double rideLength;
    private Long orderId;
    private String alertStatus; // not involved in the current project
    private String aftersalesStatus; // not involved in the current project
    private Double rideScore;
    private String rideReview;
}
