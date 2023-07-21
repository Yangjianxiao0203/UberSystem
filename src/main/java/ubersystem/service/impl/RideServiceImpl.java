package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.Enums.TimeLevel;
import ubersystem.mapper.RideMapper;
import ubersystem.mqtt.Ride.RideClient;
import ubersystem.pojo.Ride;
import ubersystem.redis.RedisClient;
import ubersystem.service.MqttService;
import ubersystem.service.RideService;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class RideServiceImpl extends MqttService implements RideService {

    @Autowired
    private RideMapper rideMapper;
    @Autowired
    private RideClient rideClient;

    @Autowired
    private RedisClient redisClient;

    @Transactional
    @Override
    public Ride createRide(Ride ride) {
        rideMapper.insert(ride);
        //publish it to mqtt ride broker, with correct topic
        MqttMessage message = getJson(ride);
        try {
            RideClient.getClient().publish(ride.getMqttChannelName(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    @Override
    public void listenToRide(String channel) {
        rideClient.subscribe(channel);
    }

    @Override
    public void publishRide(Ride ride) {
        MqttMessage message = getJson(ride);
        try {
            RideClient.getClient().publish(ride.getMqttChannelName(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void publishRides(List<Ride> rides) {

    }
    @Override
    public List<Ride> getAllRides(Long uid) {
        List<Ride> rides = rideMapper.getRideByPassengerUid(uid);
        return rides;
    }

    /**
     * get ride by rid
     */
    @Override
    public Ride getRideByRid(Long rideId){
        String key = "ride:" + rideId;
        // get from cache
        Ride ride=null;
        try {
            ride = redisClient.get(key, Ride.class);
        } catch (Exception e) {
            log.error("get ride from redis failed");
        }
        if(ride != null) {
            return ride;
        }
        // get from db
        //prevent cache penetration
        String lockKey = "lock:" + key;
        boolean isLock = redisClient.tryLock(lockKey);
        if(!isLock) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getRideByRid(rideId);
        }

        //not in cache, find in db
        ride = rideMapper.getRideById(rideId);
        //write to cache
        try {
            redisClient.set(key, ride, TimeLevel.HOUR.getValue());
        } catch (Exception e) {
            log.error("redis error when write: " + e.getMessage());
        } finally {
            redisClient.unlock(lockKey);
        }
        return ride;
    }
}
