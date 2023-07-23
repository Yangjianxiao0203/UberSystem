package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.Enums.RideStatus;
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

    /**
     * update ride
     */
    @Override
    public int updateRide(Ride ride) {
        int res = rideMapper.updateRide(ride);
        if(res<=0) {
            log.error("update ride failed");
            return res;
        }
        //update cache
        String key = "ride:" + ride.getId();
        try {
            redisClient.set(key, ride, TimeLevel.HOUR.getValue());
        } catch (Exception e) {
            log.error("redis error when write: " + e.getMessage());
        }
        return res;
    }

    @Override
    public int create(Ride ride) {
        int res = rideMapper.insert(ride);
        if(res<=0) {
            log.error("insert ride failed");
            return res;
        }
        //update cache
        String key = "ride:" + ride.getId();
        try {
            redisClient.set(key, ride, TimeLevel.HOUR.getValue());
        } catch (Exception e) {
            log.error("redis error when write: " + e.getMessage());
        }
        return res;
    }

    @Override
    public List<Ride> getRideByPassengerUidAndStatus(Long uid, RideStatus rideStatus) {
        return rideMapper.getRideByPassengerUidAndStatus(uid, rideStatus);
    }
}
