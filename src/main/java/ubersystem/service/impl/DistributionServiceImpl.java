package ubersystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
    public synchronized String driverAcceptsOrder(DriverAcceptOrderRequest request, Long rideId) {
        Ride ride=rideMapper.getRideById(rideId);
        if(ride==null) {
            throw new RuntimeException("ride not found");
        }
        if(ride.getStatus()!= RideStatus.Created) {
            throw new RuntimeException("ride has been accepted by others");
        }

        String channelName = ChannelGenerator.generateTrackChannelName(rideId);

        ride.setMqttChannelName(channelName);
        ride.setStatus(RideStatus.DriverAccepted);
        int res = rideMapper.updateRide(ride);
        if(res<=0) {
            throw new RuntimeException("update ride failed");
        }

        //发布内容，让track监听这个行程
        trackService.listenToTrack(channelName);

        return channelName;
    }

    @Transactional
    public String createOrderAndRide(OrderCreationRequest request) {
        //todo: check if user exists

        // 创建订单
        Order order = new Order();
        order.setStatus(OrderStatus.Unpaid);
        order.setCreationTime(LocalDateTime.now());
        orderMapper.insert(order);

        //todo: go to pay section, if not paid then return order id by throwing exception
        // some pay action

        order.setStatus(OrderStatus.Paid);

        // 创建行程Ride
        Ride ride = new Ride();
        ride.setPassengerUid(request.getUid());
        ride.setCreationTime(LocalDateTime.now());
        ride.setStartPointCoordinates(request.getPickUpLat() + "," + request.getPickUpLong());
        ride.setStartPointAddress(request.getPickUpResolvedAddress());
        ride.setEndPointAddress(request.getDesResolvedAddress());
        ride.setRideType(request.getType());
        ride.setStatus(RideStatus.Created);
        ride.setMqttChannelName(getRegionTopic(request.getProvince(), request.getCity()));
        ride.setOrderId(order.getId());
        rideMapper.insert(ride);

        // 更新订单的rideId
        order.setRideId(ride.getId());
        orderMapper.update(order);

        //订阅ride MQTT频道
        rideService.listenToRide(ride.getMqttChannelName());


        return ride.getId().toString();
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
        return province + "-" + city;
    }

    public List<String> getNearByVehicles (double lat, double lon) {
        List<String> res=new ArrayList<>();
        //todo : get nearby vehicles

        return res;
    }
}
