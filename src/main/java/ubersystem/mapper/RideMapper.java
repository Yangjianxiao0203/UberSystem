package ubersystem.mapper;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Ride;

import java.time.LocalDateTime;

@Mapper
public interface RideMapper {
    @Insert("INSERT INTO ride(id, creation_time, passenger_uid, driver_uid, mqtt_channel_name, ride_type, start_point_coordinates, start_point_address, end_point_coordinates, end_point_address, status, driver_accept_time, pick_up_time, arrival_time, cancellation_time, ride_length, order_id, alert_status, aftersales_status, ride_score, ride_review) " +
            "VALUES (#{ride.id}, #{ride.creationTime}, #{ride.passengerUid}, #{ride.driverUid}, #{ride.mqttChannelName}, #{ride.rideType}, #{ride.startPointCoordinates}, #{ride.startPointAddress}, #{ride.endPointCoordinates}, #{ride.endPointAddress}, #{ride.status}, #{ride.driverAcceptTime}, #{ride.pickUpTime}, #{ride.arrivalTime}, #{ride.cancellationTime}, #{ride.rideLength}, #{ride.orderId}, #{ride.alertStatus}, #{ride.aftersalesStatus}, #{ride.rideScore}, #{ride.rideReview})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createRide(@Param("ride") Ride ride);

    @Update("UPDATE ride SET status = 'Cancelled', cancellation_time = #{cancellationTime} WHERE id = #{rideId}")
    int cancelRide(@Param("rideId") Long rideId, @Param("cancellationTime") LocalDateTime cancellationTime);

    @Update("UPDATE ride SET status = 'DriverAccepted', driver_uid = #{driverUid}, driver_accept_time = #{driverAcceptTime} WHERE id = #{rideId}")
    int driverAcceptRide(@Param("rideId") Long rideId, @Param("driverUid") Long driverUid, @Param("driverAcceptTime") LocalDateTime driverAcceptTime);

    @Update("UPDATE ride SET status = 'PickedUpPassenger', pick_up_time = #{pickUpTime} WHERE id = #{rideId}")
    int pickUpPassenger(@Param("rideId") Long rideId, @Param("pickUpTime") LocalDateTime pickUpTime);

    @Update("UPDATE ride SET status = 'Arrived', arrival_time = #{arrivalTime} WHERE id = #{rideId}")
    int arriveAtDestination(@Param("rideId") Long rideId, @Param("arrivalTime") LocalDateTime arrivalTime);


    @Select("SELECT * FROM ride WHERE id = #{rideId}")
    Ride getRideById(@Param("rideId") Long rideId);
}
