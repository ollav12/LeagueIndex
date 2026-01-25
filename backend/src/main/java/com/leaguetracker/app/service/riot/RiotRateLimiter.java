package com.leaguetracker.app.service.riot;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class RiotRateLimiter {

    // Riot Games dev key limits: 20 requests per 1 second, 100 requests per 2 minutes
    private static final Bandwidth SHORT_TERM = Bandwidth.classic(20, Refill.intervally(20, Duration.ofSeconds(1)));
    private static final Bandwidth LONG_TERM = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(2)));

    // 12 buckets → safely under both limits even with bursty parallel calls
    private final List<LocalBucket> buckets = IntStream.range(0, 12)
            .mapToObj(i -> Bucket.builder()
                    .addLimit(SHORT_TERM)
                    .addLimit(LONG_TERM)
                    .build())
            .toList();

    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Blocks the current thread until a token is available (recommended for Riot API)
     */
    public void acquire() {
        int index = counter.getAndIncrement() % buckets.size();
        Bucket bucket = buckets.get(Math.abs(index)); // Math.abs for negative overflow safety (very rare)

        // This will block if rate limit is exceeded
        try {
            bucket.asBlocking().consume(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}