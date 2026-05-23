package io.github.tedredington.craigslist.http;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple monotonic-clock rate limiter that enforces a minimum delay between calls to {@link
 * #acquire()}. Thread-safe.
 */
public final class RateLimiter {

  private final long minIntervalNanos;
  private final AtomicLong nextAllowedNanos = new AtomicLong(0L);

  public RateLimiter(Duration minInterval) {
    if (minInterval == null || minInterval.isNegative()) {
      throw new IllegalArgumentException("minInterval must be non-negative");
    }
    this.minIntervalNanos = minInterval.toNanos();
  }

  /** Block (sleep) until enough time has elapsed since the last acquire. */
  public void acquire() throws InterruptedException {
    if (minIntervalNanos == 0L) {
      return;
    }
    while (true) {
      long now = System.nanoTime();
      long allowed = nextAllowedNanos.get();
      if (now >= allowed) {
        long newAllowed = now + minIntervalNanos;
        if (nextAllowedNanos.compareAndSet(allowed, newAllowed)) {
          return;
        }
      } else {
        Thread.sleep(Duration.ofNanos(allowed - now));
      }
    }
  }
}
