package ubersystem.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
public class RedisClient {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> void set(String key, T value, long time) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(value);
        log.info("set redis key: " + key + " value: " + json + " time: " + time);
        redisTemplate.opsForValue().set(key, json,time, TimeUnit.SECONDS);
    }

    public <T> T get(String key, Class<T> clazz) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get(key);
        if(json == null) {
            return null;
        }
        T obj = objectMapper.readValue(json, clazz);
        return obj;
    }
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 利用redis实现互斥锁，如果key不存在则设置key并返回true，否则返回false
    public boolean tryLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "1");
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
