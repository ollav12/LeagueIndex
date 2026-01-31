package com.leaguetracker.app.service.riot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class RiotRateLimiterIntegrationTest {

    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    RedisTemplate<String, Integer> redisTemplate;

    RiotRateLimiter riotRateLimiter;

    @BeforeEach
    void setup() {
        // Flush Redis before each test
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        riotRateLimiter = new RiotRateLimiter(redisTemplate);
    }

    @Test
    void shouldAllowRequestsWithinShortTermLimit() throws InterruptedException {
        // Should allow 20 requests immediately
        for (int i = 0; i < 20; i++) {
            riotRateLimiter.acquire();
        }

        // The 21st request would block; we can test by a timeout
        Thread thread = new Thread(riotRateLimiter::acquire);
        thread.start();
        try {
            thread.join(50); // 50ms wait
            // If thread still alive, it's correctly blocked
            assert (thread.isAlive());
        } finally {
            thread.interrupt();
        }
    }

    @Test
    void shouldRespectLongTermLimit() throws InterruptedException {
        // Send 100 requests (long-term limit)
        for (int i = 0; i < 100; i++) {
            riotRateLimiter.acquire();
        }

        // The 101st request should block
        Thread thread = new Thread(riotRateLimiter::acquire);
        thread.start();
        thread.join(50); // 50ms wait
        assert (thread.isAlive());
        thread.interrupt();
    }

    @Test
    void shortTermCounterResets() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            riotRateLimiter.acquire();
        }

        // Wait longer than short-term window (1s)
        Thread.sleep(1100);

        // Now 20 requests should be allowed again
        for (int i = 0; i < 20; i++) {
            riotRateLimiter.acquire();
        }
    }
}
