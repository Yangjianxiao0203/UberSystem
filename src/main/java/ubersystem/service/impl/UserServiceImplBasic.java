package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;
import ubersystem.Enums.TimeLevel;
import ubersystem.mapper.RideMapper;
import ubersystem.mapper.UserMapper;
import ubersystem.pojo.Ride;
import ubersystem.pojo.User;
import ubersystem.redis.RedisClient;
import ubersystem.service.LogService;
import ubersystem.service.RideService;
import ubersystem.service.UserService;
import ubersystem.utils.JwtUtils;

@Service
@Slf4j
public class UserServiceImplBasic implements UserService {

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private RideMapper rideMapper;
    @Autowired
    private RideService rideService;

    @Autowired
    private RedisClient redisClient;


    @Override
    public int registerUser(User user) {
        //if already exist return false
        if (userMapper.getUserByPhoneNumber(user.getPhoneNumber()) != null) {
            log.error("user already exist");
            return -1;
        }

        int res = userMapper.insertUser(user);
        log.info("register user in database: "+ (res>0));
        return res;
    }

    @Override
    public User login(String phoneNumber, String password) {
        User user = userMapper.getUserByPhoneNumber(phoneNumber);
        if (user == null) {
            log.error("user not exist");
            return null;
        }
        if (!user.getSecretKey().equals(password)) {
            log.error("password not match");
            return null;
        }
        return user;
    }

    @Override
    public String createToken(User user) {
        return JwtUtils.generateToken(user);
    }

    @Override
    public User getUserByUid(Long uid) {
        String key = "user:" + uid;
        User user=null;
        try {
            user = redisClient.get(key, User.class);
        } catch (Exception e) {
            log.error("redis error when read: " + e.getMessage());
        }

        if(user != null) {
            return user;
        }

        // prevent cache penetration
        String lockKey = "lock:" + key;
        boolean isLock = redisClient.tryLock(lockKey);

        if(!isLock) {
            // if lock failed, sleep 100ms and try again
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getUserByUid(uid);
        }

        // not in cache, find it in database
        user = userMapper.getUserByUid(uid);
        //write to cache
        try {
            redisClient.set(key, user, TimeLevel.HOUR.getValue());
        } catch (Exception e) {
            log.error("redis error when write: " + e.getMessage());
        } finally {
            redisClient.unlock(lockKey);
        }

        return userMapper.getUserByUid(uid);
    }

    @Override
    public int updateUser(User user) {
        //update database
        int res = userMapper.updateUser(user);
        if(res<=0) {
            log.error("update user failed");
            return res;
        }
        //update cache
        String key = "user:" + user.getUid();
        try {
            redisClient.set(key, user, TimeLevel.HOUR.getValue());
        } catch (Exception e) {
            log.error("redis error when write: " + e.getMessage());
        }
        return res;
    }

    @Override
    public int deleteUser(Long uid) {
        int res = userMapper.deleteUser(uid);
        if(res<=0) {
            log.error("delete user failed");
            return res;
        }
        //delete cache
        String key = "user:" + uid;
        try {
            redisClient.delete(key);
        } catch (Exception e) {
            log.error("redis error when delete: " + e.getMessage());
        }

        return userMapper.deleteUser(uid);
    }

    @Override
    public User getDriverByRid(Long rid) {
        Ride ride = rideService.getRideByRid(rid);
        if (ride == null) {
            log.error("ride not exist");
            throw new RuntimeException("ride not exist");
        }
        Long uid= ride.getDriverUid();
        if (uid == null) {
            log.error("driver not exist");
            throw new RuntimeException("driver not exist");
        }
        return this.getUserByUid(uid);
    }
}
