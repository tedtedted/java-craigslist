package io.github.tedtedted.craigslist.http;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/** Exponential-backoff retry policy with full jitter. Immutable; share freely. */
public record RetryPolicy(
    int maxAttempts, Duration initialBackoff, double multiplier, Duration maxBackoff) {

  public static RetryPolicy defaults() {
    return new RetryPolicy(3, Duration.ofMillis(500), 2.0, Duration.ofSeconds(10));
  }

  public RetryPolicy {
    if (maxAttempts < 1) {
      throw new IllegalArgumentException("maxAttempts must be >= 1");
    }
    if (initialBackoff.isNegative() || initialBackoff.isZero()) {
      throw new IllegalArgumentException("initialBackoff must be > 0");
    }
    if (multiplier < 1.0) {
      throw new IllegalArgumentException("multiplier must be >= 1.0");
    }
    if (maxBackoff.isNegative() || maxBackoff.isZero()) {
      throw new IllegalArgumentException("maxBackoff must be > 0");
    }
  }

  /** Returns the delay to wait before the {@code attempt}-th retry (1-indexed). */
  public Duration backoffFor(int attempt) {
    double rawMillis = initialBackoff.toMillis() * Math.pow(multiplier, attempt - 1);
    long capped = Math.min((long) rawMillis, maxBackoff.toMillis());
    long jittered = ThreadLocalRandom.current().nextLong(0, Math.max(capped, 1));
    return Duration.ofMillis(jittered);
  }
}
