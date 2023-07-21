package ubersystem.service;

import org.springframework.transaction.annotation.Transactional;
import ubersystem.pojo.Ride;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void listenToRide(String channel);

    @Transactional
    public void publishRide(Ride ride);

    @Transactional
    public void publishRides(List<Ride> rides);

    @Transactional
    public List<Ride> getAllRides(Long uid);

    @Transactional
    public Ride getRideByRid(Long rideId);
}
