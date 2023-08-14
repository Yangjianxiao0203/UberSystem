package ubersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.pojo.*;
import ubersystem.pojo.request.distribution.DriverAcceptOrderRequest;
import ubersystem.pojo.request.distribution.OrderCreationRequest;

import java.util.List;

@Service
public interface DistributionService {
    @Transactional
    public String driverAcceptsOrder(DriverAcceptOrderRequest request, Long rideId);

    public String createOrderAndRide(OrderCreationRequest request);

    @Transactional
    public RideAndNearbyVehicles getRideAndNearByVehicles(long rid, double lat, double lon);

    @Transactional
    public String cancelRideAndOrder(Long rid, Long uid, boolean cancel);

    @Transactional
    public List<Ride> getAcceptedRide(Long uid);

    @Transactional
    public String getTrackIdByRid(Long rid);

    @Transactional
    public List<Ride> getCreatedRidesByChannelName(String channelName);
}
