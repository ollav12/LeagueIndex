package com.leaguetracker.app.service;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final int ttl; // TTL in seconds

    public RateLimiterService(
            RedisTemplate<String, Integer> redisTemplate, @Value("${ratelimiter.ttl:60}") int ttl) { // default 60s
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }

    public boolean isAllowed(String ip) {
        String key = "rate_limit:" + ip;
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        Long count = ops.increment(key);
        if (count == null) {
            throw new IllegalStateException("Redis INCR returned null for key: " + key);
        }

        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(ttl));
        }

        return count <= 10;
    }
}
