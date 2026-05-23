package io.github.tedredington.craigslist.http;

import io.github.tedredington.craigslist.exception.CraigslistBlockedException;
import io.github.tedredington.craigslist.exception.CraigslistHttpException;
import io.github.tedredington.craigslist.exception.CraigslistNetworkException;
import io.github.tedredington.craigslist.exception.CraigslistNotFoundException;
import io.github.tedredington.craigslist.exception.CraigslistRateLimitException;
import io.github.tedredington.craigslist.exception.CraigslistServerException;
import io.github.tedredington.craigslist.exception.CraigslistTimeoutException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** GETs Craigslist URLs with retry, rate limiting, and exception mapping.
 *  Returns Jsoup {@link Document} instances with the request URI set as the base
 *  so relative links resolve correctly. */
public final class HttpFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(HttpFetcher.class);

    private final HttpClient httpClient;
    private final RetryPolicy retryPolicy;
    private final RateLimiter rateLimiter;
    private final String userAgent;
    private final Duration requestTimeout;

    public HttpFetcher(
            HttpClient httpClient,
            RetryPolicy retryPolicy,
            RateLimiter rateLimiter,
            String userAgent,
            Duration requestTimeout) {
        this.httpClient = httpClient;
        this.retryPolicy = retryPolicy;
        this.rateLimiter = rateLimiter;
        this.userAgent = userAgent;
        this.requestTimeout = requestTimeout;
    }

    public Document getDocument(URI uri) {
        String body = getString(uri);
        return Jsoup.parse(body, uri.toString());
    }

    public String getString(URI uri) {
        IOException lastIo = null;
        int attempt = 0;
        while (attempt < retryPolicy.maxAttempts()) {
            attempt++;
            try {
                rateLimiter.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CraigslistNetworkException("interrupted while rate-limiting", uri, e);
            }
            LOG.debug("GET {} (attempt {})", uri, attempt);
            HttpRequest req =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .timeout(requestTimeout)
                            .header("User-Agent", userAgent)
                            .header("Accept", "text/html,application/xhtml+xml")
                            .GET()
                            .build();
            try {
                HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                int status = resp.statusCode();
                if (status >= 200 && status < 300) {
                    return resp.body();
                }
                if (status == 429) {
                    Duration retryAfter = parseRetryAfter(resp).orElse(null);
                    if (attempt < retryPolicy.maxAttempts()) {
                        Duration sleep = retryAfter != null ? retryAfter : retryPolicy.backoffFor(attempt);
                        LOG.warn("429 from {} — sleeping {} before retry", uri, sleep);
                        sleep(sleep, uri);
                        continue;
                    }
                    throw new CraigslistRateLimitException(uri, resp.body(), attempt, retryAfter);
                }
                if (status >= 500 && status < 600) {
                    if (attempt < retryPolicy.maxAttempts()) {
                        Duration sleep = retryPolicy.backoffFor(attempt);
                        LOG.warn("HTTP {} from {} — retrying in {}", status, uri, sleep);
                        sleep(sleep, uri);
                        continue;
                    }
                    throw new CraigslistServerException(status, uri, resp.body(), attempt);
                }
                if (status == 403) {
                    throw new CraigslistBlockedException(uri, resp.body(), attempt);
                }
                if (status == 404) {
                    throw new CraigslistNotFoundException(uri, resp.body(), attempt);
                }
                throw new CraigslistHttpException(status, uri, resp.body(), attempt);
            } catch (HttpTimeoutException e) {
                if (attempt < retryPolicy.maxAttempts()) {
                    Duration sleep = retryPolicy.backoffFor(attempt);
                    LOG.warn("timeout from {} — retrying in {}", uri, sleep);
                    sleep(sleep, uri);
                    continue;
                }
                throw new CraigslistTimeoutException("request timed out", uri, e);
            } catch (IOException e) {
                lastIo = e;
                if (attempt < retryPolicy.maxAttempts()) {
                    Duration sleep = retryPolicy.backoffFor(attempt);
                    LOG.warn("I/O error from {} — retrying in {}: {}", uri, sleep, e.toString());
                    sleep(sleep, uri);
                    continue;
                }
                throw new CraigslistNetworkException("network I/O error", uri, e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CraigslistNetworkException("interrupted during request", uri, e);
            }
        }
        throw new CraigslistNetworkException(
                "retries exhausted", uri, lastIo == null ? new IOException("no response") : lastIo);
    }

    private static Optional<Duration> parseRetryAfter(HttpResponse<?> resp) {
        return resp.headers()
                .firstValue("Retry-After")
                .map(String::trim)
                .flatMap(
                        v -> {
                            try {
                                return Optional.of(Duration.ofSeconds(Long.parseLong(v)));
                            } catch (NumberFormatException nfe) {
                                return Optional.empty();
                            }
                        });
    }

    private static void sleep(Duration d, URI uri) {
        try {
            Thread.sleep(d.toMillis(), d.toNanosPart() % 1_000_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CraigslistNetworkException("interrupted during backoff", uri, e);
        }
    }
}
