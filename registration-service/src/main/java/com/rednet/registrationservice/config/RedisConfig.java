package com.rednet.registrationservice.config;

import com.rednet.registrationservice.entity.Registration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    private final String host;
    private final int port;

    public RedisConfig(@Value("${spring.data.redis.host}") String host,
                       @Value("${spring.data.redis.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    public LettuceConnectionFactory registrationRedisFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisTemplate<String, Registration> registrationRedisTemplate() {
        RedisTemplate<String, Registration> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(registrationRedisFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Registration.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Registration.class));
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
