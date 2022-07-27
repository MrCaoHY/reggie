package com.example.reggie.config;

import com.example.reggie.common.JacksonObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 通过继承CachingConfigurerSupport，将Spring-Cache的缓存换为Redis
 * @author caohaiyang
 * @date 2022/7/27 2:56
 * @param null
 * @return null
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(RedisSerializer.string());

        Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        objectJackson2JsonRedisSerializer.setObjectMapper(new JacksonObjectMapper());

//        redisTemplate.setValueSerializer(RedisSerializer.json());
        //默认的对LocalDateTime序列化有问题 所以把前面的自定义序列化加进来自定义一个
        redisTemplate.setValueSerializer(objectJackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;

    }
}
