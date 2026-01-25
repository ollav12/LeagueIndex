package com.leaguetracker.app.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, Integer> redisTemplate;

    public RateLimiterService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String ip) {
        String key = "ratelimit::" + ip; // key prefix
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        Integer current = ops.get(key);
        if (current == null) {
            ops.set(key, 1, Duration.ofSeconds(60)); // 1-minute window
            return true;
        } else if (current < 10) { // limit = 10 requests
            ops.increment(key);
            return true;
        } else {
            return false;
        }
    }
}
