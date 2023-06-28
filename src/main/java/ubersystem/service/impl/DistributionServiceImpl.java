package ubersystem.service.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.Enums.OrderStatus;
import ubersystem.Enums.RideStatus;
import ubersystem.mapper.OrderMapper;
import ubersystem.mapper.RideMapper;
import ubersystem.mqtt.Track.TrackClient;
import ubersystem.pojo.DriverAcceptOrderRequest;
import ubersystem.pojo.Order;
import ubersystem.pojo.OrderCreationRequest;
import ubersystem.pojo.Ride;
import ubersystem.service.DistributionService;
import ubersystem.service.RideService;
import ubersystem.service.TrackService;
import ubersystem.utils.ChannelGenerator;

import java.time.LocalDateTime;

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
    public synchronized String driverAcceptsOrder(DriverAcceptOrderRequest request) {
        Ride ride=rideMapper.getRideById(request.getDriverUid());
        if(ride==null) {
            throw new RuntimeException("ride not found");
        }
        if(ride.getStatus()!= RideStatus.Created) {
            throw new RuntimeException("ride has been accepted by others");
        }

        String channelName = ChannelGenerator.generateTrackChannelName(request.getDriverUid());

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

    private String getRegionTopic(String province, String city) {
        return province + "-" + city;
    }
}
