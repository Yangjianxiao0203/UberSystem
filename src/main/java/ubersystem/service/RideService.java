package ubersystem.service;

import org.springframework.transaction.annotation.Transactional;
import ubersystem.Enums.RideStatus;
import ubersystem.pojo.Ride;

import java.time.LocalDateTime;
import java.util.List;

public interface RideService {

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

    @Transactional
    public int create(Ride ride);

    @Transactional
    public int updateRide(Ride ride);

    @Transactional
    public List<Ride> getRideByPassengerUidAndStatus(Long uid, RideStatus rideStatus);

    @Transactional
    public List<Ride> getRidesByChannelNameAndStatus(String channelName,RideStatus rideStatus);
}
