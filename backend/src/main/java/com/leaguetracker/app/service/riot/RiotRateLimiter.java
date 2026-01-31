package com.leaguetracker.app.service.riot;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RiotRateLimiter {

    private final RedisTemplate<String, Integer> redisTemplate;

    private static final int SHORT_TERM_LIMIT = 20;
    private static final int LONG_TERM_LIMIT = 100;

    private static final Duration SHORT_TERM_WINDOW = Duration.ofSeconds(1);
    private static final Duration LONG_TERM_WINDOW = Duration.ofMinutes(2);

    private final String apiKey = "riot-api-key";

    public RiotRateLimiter(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public synchronized void acquire() {
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        String shortTermKey = "riot_rate:short:" + apiKey;
        String longTermKey = "riot_rate:long:" + apiKey;

        while (true) {
            Integer shortCount = ops.get(shortTermKey);
            Integer longCount = ops.get(longTermKey);

            shortCount = (shortCount == null) ? 0 : shortCount;
            longCount = (longCount == null) ? 0 : longCount;

            if (shortCount < SHORT_TERM_LIMIT && longCount < LONG_TERM_LIMIT) {
                // Increment counters and set TTL if first hit
                ops.increment(shortTermKey);
                ops.increment(longTermKey);

                if (shortCount == 0) {
                    redisTemplate.expire(shortTermKey, SHORT_TERM_WINDOW);
                }
                if (longCount == 0) {
                    redisTemplate.expire(longTermKey, LONG_TERM_WINDOW);
                }
                return; // Allowed
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
