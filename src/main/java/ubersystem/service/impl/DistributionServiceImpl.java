package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.Enums.OrderStatus;
import ubersystem.Enums.RideStatus;
import ubersystem.mapper.OrderMapper;
import ubersystem.mapper.RideMapper;
import ubersystem.pojo.*;
import ubersystem.pojo.request.distribution.DriverAcceptOrderRequest;
import ubersystem.pojo.request.distribution.OrderCreationRequest;
import ubersystem.service.DistributionService;
import ubersystem.service.RideService;
import ubersystem.service.TrackService;
import ubersystem.utils.ChannelGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class DistributionServiceImpl implements DistributionService {

    @Autowired
    private RideMapper rideMapper;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TrackService trackService;

    @Autowired
    private RideService rideService;

    @Override
    @Transactional
    public synchronized String driverAcceptsOrder(DriverAcceptOrderRequest request, Long rideId) {
        log.info("driverUid: {} try to accept ride: {}", request.getDriverUid(), rideId);
        Ride ride=rideMapper.getRideById(rideId);
        if(ride==null) {
            throw new RuntimeException("ride not found");
        }
        if(ride.getStatus()!= RideStatus.Created) {
            throw new RuntimeException("ride has been accepted by others");
        }

        String channelName = ChannelGenerator.generateTrackChannelName(rideId);

        ride.setStatus(RideStatus.DriverAccepted);
        ride.setDriverUid(request.getDriverUid());

        int res = rideMapper.updateRide(ride);
        if(res<=0) {
            throw new RuntimeException("update ride failed");
        }
        //发布内容，让track监听这个行程
        trackService.listenToTrack(channelName);

        // create track
        Track track = new Track();
        track.setRideId(rideId);
        track.setMqttChannelName(channelName);
        track.setTimeSequence(LocalDateTime.now());
        trackService.createTrack(track);

        return channelName;
    }

    @Override
    public String getTrackIdByRid(Long rid) {
        Track track = trackService.getTrackByRid(rid);
        if(track==null) {
            throw new RuntimeException("track not found");
        }
        return track.getMqttChannelName();
    }

    @Transactional
    public String createOrderAndRide(OrderCreationRequest request) {
        //todo: check if user exists

        // 创建订单
        Order order = new Order();
        order.setStatus(OrderStatus.Unpaid);
        order.setCreationTime(LocalDateTime.now());
        orderMapper.insert(order);

        order.setStatus(OrderStatus.Unpaid);

        // 创建行程Ride
        Ride ride = new Ride();
        ride.setPassengerUid(request.getUid());
        ride.setCreationTime(LocalDateTime.now());
        ride.setStartPointCoordinates(request.getPickUpLat() + "," + request.getPickUpLong());
        ride.setStartPointAddress(request.getPickUpResolvedAddress());
        ride.setEndPointAddress(request.getDesResolvedAddress());
        ride.setEndPointCoordinates(request.getDesResolvedAddress());
        ride.setRideType(request.getType());
        ride.setStatus(RideStatus.Created);
        ride.setMqttChannelName(getRegionTopic(request.getProvince(), request.getCity()));
        ride.setOrderId(order.getId());

        ride.setRideLength(getDistance(ride.getStartPointAddress(), ride.getEndPointAddress()));
        rideMapper.insert(ride);

        // 更新订单的rideId
        order.setRideId(ride.getId());
        orderMapper.update(order);

        //订阅ride MQTT频道
        rideService.listenToRide(ride.getMqttChannelName());

        //发布ride
        rideService.publishRide(ride);

        return ride.getId().toString();
    }

    @Override
    @Transactional
    public List<Ride> getAcceptedRide(Long uid) {
        List<Ride> rides = rideMapper.getRideByPassengerUid(uid);
        // remain the DriverAccepted rides
        List<Ride> res = new ArrayList<>();
        for(Ride ride: rides) {
            if(ride.getStatus()== RideStatus.DriverAccepted) {
                res.add(ride);
            }
        }
        return res;
    }


    @Override
    @Transactional
    public RideAndNearbyVehicles getRideAndNearByVehicles(long rid, double lat, double lon) {
        Ride ride = rideMapper.getRideById(rid);
        if(ride==null) {
            throw new RuntimeException("ride not found");
        }
        List<String> vehicles = getNearByVehicles(lat, lon);
        RideAndNearbyVehicles res = new RideAndNearbyVehicles(ride,vehicles);

        return res;
    }

    @Override
    public String cancelRideAndOrder(Long rid, Long uid, boolean cancel) {
        Order order = orderMapper.getOrderByRideId(rid);
        if(order==null) {
            throw new RuntimeException("order not found");
        }
        Ride ride = rideMapper.getRideById(order.getRideId());
        if(ride==null) {
            throw new RuntimeException("ride not found");
        }
        boolean isDriver = Objects.equals(ride.getDriverUid(), uid);
        boolean isPassenger = Objects.equals(ride.getPassengerUid(), uid);
        if(!isDriver && !isPassenger) {
            throw new RuntimeException("user not match");
        }

        // todo: order cancel and refunded operation
        order.setStatus(OrderStatus.RefundProcessing);

        ride.setStatus(RideStatus.Cancelled);

        //update both in database
        rideMapper.updateRide(ride);
        orderMapper.update(order);

        return "cancel success";
    }

    private String getRegionTopic(String province, String city) {
        return "ride-"+province + "-" + city;
    }

    public List<String> getNearByVehicles (double lat, double lon) {
        List<String> res=new ArrayList<>();
        //todo : get nearby vehicles

        return res;
    }

    public double getDistance(String start, String end) {
        String[] startArr=start.split(",");
        String[] endArr=end.split(",");
        double lat1=Double.parseDouble(startArr[0]);
        double lon1=Double.parseDouble(startArr[1]);
        double lat2=Double.parseDouble(endArr[0]);
        double lon2=Double.parseDouble(endArr[1]);

        return getDistance(lat1, lon1, lat2, lon2);
    }
    public double getDistance(double lat1,double lon1, double lat2, double lon2) {
        double R = 6371.393; // km
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }
}
