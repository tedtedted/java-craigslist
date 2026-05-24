package io.github.tedtedted.craigslist.exception;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

/**
 * Thrown when Craigslist responded with HTTP 429 (Too Many Requests). When the response included a
 * {@code Retry-After} header, its value is exposed via {@link #retryAfter()} so callers can wait
 * the suggested amount before retrying.
 */
public class CraigslistRateLimitException extends CraigslistHttpException {

  private final Duration retryAfter;

  public CraigslistRateLimitException(
      URI uri, String responseBody, int attemptCount, Duration retryAfter) {
    super(429, uri, responseBody, attemptCount);
    this.retryAfter = retryAfter;
  }

  public Optional<Duration> retryAfter() {
    return Optional.ofNullable(retryAfter);
  }
}
