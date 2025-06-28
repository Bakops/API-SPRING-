package com.knowly.rest_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RedisCleaner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        System.out.println("✅ Redis vidé au démarrage.");
    }
}
