package egovframework.com.devjitsu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${Globals.redis.dataBase}")
    private String[] databases;

    private final GenericApplicationContext context;

    public RedisConfig(GenericApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < databases.length; i++) {
            int dbIndex = Integer.parseInt(databases[i]);
            boolean isPrimaryFactory = (dbIndex == 0);

            String factoryBeanName = "redisConnectionFactory" + dbIndex;
            context.registerBean(factoryBeanName, RedisConnectionFactory.class, () -> createRedisConnectionFactory(dbIndex), beanDefinition -> {
                if (isPrimaryFactory) {
                    beanDefinition.setPrimary(true); // Primary 설정
                }
            });
            String templateBeanName = "redisTemplate" + dbIndex;
            context.registerBean(templateBeanName, RedisTemplate.class,
                () -> redisTemplate(context.getBean(factoryBeanName, RedisConnectionFactory.class)), beanDefinition -> {
                    if (isPrimaryFactory) {
                        beanDefinition.setPrimary(true); // Primary 설정
                    }
                });
        }
    }

    private RedisConnectionFactory createRedisConnectionFactory(int index) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setDatabase(index);
        config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }

    private RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}

