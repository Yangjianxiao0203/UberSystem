package ubersystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.dto.RideMapper;
import ubersystem.pojo.Ride;
import ubersystem.service.RideService;

import java.time.LocalDateTime;

@Service
public class RideServiceImpl implements RideService {

    private RideMapper rideMapper;
    @Autowired
    public RideServiceImpl(RideMapper rideMapper) {
        this.rideMapper = rideMapper;
    }

    @Override
    public Ride createRide(Ride ride) {
        rideMapper.createRide(ride);
        return ride;
    }

    @Override
    public boolean cancelRide(Long rideId, LocalDateTime cancellationTime) {
        int result = rideMapper.cancelRide(rideId, cancellationTime);
        return result>0;
    }

    @Override
    public boolean driverAcceptRide(Long rideId, Long driverUid, LocalDateTime driverAcceptTime) {
        int result = rideMapper.driverAcceptRide(rideId, driverUid, driverAcceptTime);
        return result>0;
    }

    @Override
    public boolean pickUpPassenger(Long rideId, LocalDateTime pickUpTime) {
        return rideMapper.pickUpPassenger(rideId, pickUpTime)>0;
    }
}
