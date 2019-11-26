package com.klausner.websocket.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Bean
    public Jedis jedis(@Value("${redis.client.url:localhost}") String redisUrl,
                       @Value("${redis.client.port:6379}") Integer redisPort) {
        return new Jedis(redisUrl, redisPort);
    }
}
