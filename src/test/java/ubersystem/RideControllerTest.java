package ubersystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ubersystem.Enums.RideStatus;
import ubersystem.Enums.RideType;
import ubersystem.controller.RideController;
import ubersystem.pojo.Result;
import ubersystem.pojo.Ride;
import ubersystem.service.RideService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RideControllerTest {
    @MockBean
    private RideService rideService;

    private RideController rideController;

    @BeforeEach
    public void setup() {
        this.rideController = new RideController(rideService);
    }
    @Test
    public void testCreateRide() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setCreationTime(LocalDateTime.parse("2023-05-24T15:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        ride.setPassengerUid(123L);
        // The driverUid is null, so we don't need to set it
        ride.setMqttChannelName("channel1");
        ride.setRideType(RideType.valueOf("Economy"));
        ride.setStartPointCoordinates("34.0522,-118.2437");
        ride.setStartPointAddress("Los Angeles, CA");
        ride.setEndPointCoordinates("37.7749,-122.4194");
        ride.setEndPointAddress("San Francisco, CA");
        ride.setStatus(RideStatus.valueOf("Created"));
        // The driverAcceptTime, pickUpTime, and arrivalTime are null, so we don't need to set them
        ride.setOrderId(12345L);
        // The alertStatus, aftersalesStatus, rideScore, and rideReview are null, so we don't need to set them

        // Mock the service call
        when(rideService.createRide(any(Ride.class))).thenReturn(ride);

        Result<Ride> result = rideController.createRide(ride);
        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());
    }

    @Test
    public void testpickUpPassenger() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setCreationTime(LocalDateTime.parse("2023-05-24T15:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        ride.setPassengerUid(123L);
        // The driverUid is null, so we don't need to set it
        ride.setMqttChannelName("channel1");
        ride.setRideType(RideType.valueOf("Economy"));
        ride.setStartPointCoordinates("34.0522,-118.2437");
        ride.setStartPointAddress("Los Angeles, CA");
        ride.setEndPointCoordinates("37.7749,-122.4194");
        ride.setEndPointAddress("San Francisco, CA");
        ride.setStatus(RideStatus.valueOf("Created"));
        // The driverAcceptTime, pickUpTime, and arrivalTime are null, so we don't need to set them
        ride.setOrderId(12345L);
        // The alertStatus, aftersalesStatus, rideScore, and rideReview are null, so we don't need to set them

        // Simulate that the driver has picked up the passenger
        LocalDateTime pickUpTime = LocalDateTime.now();
        when(rideService.pickUpPassenger(ride.getId(), pickUpTime)).thenReturn(true);

        Result<Ride> result = rideController.createRide(ride);
        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());
    }
}
