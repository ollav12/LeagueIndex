package com.leaguetracker.app.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = "ratelimiter.ttl=2") // TTL = 2 seconds for fast tests
class RateLimiterServiceIntegrationTest {

    private static final String TEST_IP = "1.2.3.4";

    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    RateLimiterService rateLimiterService;

    @Autowired
    RedisTemplate<String, ?> redisTemplate;

    @BeforeEach
    void setup() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void shouldEnforceLimitAcrossCalls() {
        for (int i = 1; i <= 10; i++) {
            assertTrue(rateLimiterService.isAllowed(TEST_IP));
        }

        assertFalse(rateLimiterService.isAllowed(TEST_IP));
    }

    @Test
    void differentIpsShouldHaveSeparateLimits() {
        String ip2 = "5.6.7.8";

        for (int i = 1; i <= 10; i++) {
            assertTrue(rateLimiterService.isAllowed(TEST_IP));
            assertTrue(rateLimiterService.isAllowed(ip2));
        }

        assertFalse(rateLimiterService.isAllowed(TEST_IP));
        assertFalse(rateLimiterService.isAllowed(ip2));
    }

    @Test
    void limitResetsAfterWindow() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            assertTrue(rateLimiterService.isAllowed(TEST_IP));
        }
        assertFalse(rateLimiterService.isAllowed(TEST_IP));

        Thread.sleep(2500);

        assertTrue(rateLimiterService.isAllowed(TEST_IP)); // Counter should reset
    }

    @Test
    void concurrentRequestsShouldRespectLimit() throws InterruptedException {
        Runnable requestTask = () -> assertTrue(rateLimiterService.isAllowed(TEST_IP));

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(requestTask);
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        assertFalse(rateLimiterService.isAllowed(TEST_IP));
    }
}
