package egovframework.com.devjitsu.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final GenericApplicationContext context;

    public RedisService(GenericApplicationContext context) {
        this.context = context;
    }

    public void saveRedis(int templateIndex, String key, Object value) {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate(templateIndex);
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getRedis(int templateIndex, String key) {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate(templateIndex);
        return redisTemplate.opsForValue().get(key);
    }

    public void delRedis(int templateIndex, String key) {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate(templateIndex);
        redisTemplate.delete(key);
    }

    private RedisTemplate<String, Object> getRedisTemplate(int templateIndex) {
        String beanName = "redisTemplate" + templateIndex;
        return (RedisTemplate<String, Object>) context.getBean(beanName);
    }
}