package ubersystem.service;

import org.springframework.transaction.annotation.Transactional;
import ubersystem.pojo.Ride;

import java.time.LocalDateTime;

public interface RideService {
    @Transactional
    public Ride createRide(Ride ride);
    @Transactional
    public boolean cancelRide(Long rideId, LocalDateTime cancellationTime);
    @Transactional
    public boolean driverAcceptRide(Long rideId, Long driverUid, LocalDateTime driverAcceptTime);

    @Transactional
    public boolean pickUpPassenger(Long rideId, LocalDateTime pickUpTime);
    @Transactional
    public boolean arriveAtDestination(Long rideId, LocalDateTime arrivalTime);
}
