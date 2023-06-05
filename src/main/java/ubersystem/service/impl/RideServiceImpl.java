package ubersystem.service.impl;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.mapper.RideMapper;
import ubersystem.mqtt.Ride.RideClient;
import ubersystem.pojo.Ride;
import ubersystem.service.RideService;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class RideServiceImpl implements RideService {

    private RideMapper rideMapper;
    @Autowired
    public RideServiceImpl(RideMapper rideMapper) {
        this.rideMapper = rideMapper;
    }

    @Transactional
    @Override
    public Ride createRide(Ride ride) {
        rideMapper.createRide(ride);
        //publish it to mqtt ride broker, with correct topic
        MqttMessage message = getJson(ride);
        try {
            RideClient.getClient().publish(ride.getMqttChannelName(), message);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean arriveAtDestination(Long rideId, LocalDateTime arrivalTime) {
        int rows = rideMapper.arriveAtDestination(rideId, arrivalTime);
        return rows > 0;
    }


    public MqttMessage getJson(Ride ride) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = null;
        try {
            json = mapper.writeValueAsString(ride);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MqttMessage message = new MqttMessage();
        message.setPayload(json.getBytes());
        return message;
    }

}
