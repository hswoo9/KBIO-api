package egovframework.com.devjitsu.service.redis;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisApiService {

    private final GenericApplicationContext context;

    public RedisApiService(GenericApplicationContext context) {
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