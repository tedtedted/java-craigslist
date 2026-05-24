package io.github.tedtedted.craigslist;

import io.github.tedtedted.craigslist.http.HttpFetcher;
import io.github.tedtedted.craigslist.http.RateLimiter;
import io.github.tedtedted.craigslist.http.RetryPolicy;
import io.github.tedtedted.craigslist.http.UriBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point to the library. Construct with {@link #create()} for sensible defaults or with {@link
 * #builder()} for full control over HTTP behavior. Thread-safe; intended to be a long-lived
 * singleton. Implements {@link AutoCloseable} so it can be used in try-with-resources to release
 * the underlying {@link HttpClient}.
 *
 * <p>Once you have a {@code Craigslist}, build per-category queries:
 *
 * <pre>{@code
 * Craigslist cl = Craigslist.create();
 * CraigslistHousing housing = CraigslistHousing.builder(cl)
 *     .site(Site.SF_BAY).area(Area.SFC)
 *     .minPrice(1500).maxPrice(3000)
 *     .build();
 * for (Listing l : housing) System.out.println(l);
 *
 * }</pre>
 */
public final class Craigslist implements AutoCloseable {

  private static final Logger LOG = LoggerFactory.getLogger(Craigslist.class);

  /** Default User-Agent. Override via {@link Builder#userAgent(String)}. */
  public static final String DEFAULT_USER_AGENT =
      "java-craigslist/0.1 (+https://github.com/tedtedted/java-craigslist)";

  private final HttpClient httpClient;
  private final HttpFetcher fetcher;
  private final UriBuilder uriBuilder;

  private Craigslist(HttpClient httpClient, HttpFetcher fetcher, UriBuilder uriBuilder) {
    this.httpClient = httpClient;
    this.fetcher = fetcher;
    this.uriBuilder = uriBuilder;
  }

  /** Create a {@code Craigslist} client with sensible defaults. */
  public static Craigslist create() {
    return builder().build();
  }

  /** Begin building a {@code Craigslist} client with custom HTTP behavior. */
  public static Builder builder() {
    return new Builder();
  }

  /** Internal — used by category subclasses. */
  public HttpFetcher fetcher() {
    return fetcher;
  }

  /** Internal — used by category subclasses. */
  public UriBuilder uriBuilder() {
    return uriBuilder;
  }

  @Override
  public void close() {
    LOG.info("closing Craigslist client and releasing HttpClient");
    httpClient.close();
  }

  /** Configures HTTP behavior for a {@link Craigslist} instance. */
  public static final class Builder {
    private String userAgent = DEFAULT_USER_AGENT;
    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration requestTimeout = Duration.ofSeconds(30);
    private RetryPolicy retryPolicy = RetryPolicy.defaults();
    private Duration minDelayBetweenRequests = Duration.ZERO;
    private HttpClient httpClient;
    private URI baseUriOverride;

    private Builder() {}

    public Builder userAgent(String ua) {
      this.userAgent = ua;
      return this;
    }

    public Builder connectTimeout(Duration d) {
      this.connectTimeout = d;
      return this;
    }

    public Builder requestTimeout(Duration d) {
      this.requestTimeout = d;
      return this;
    }

    public Builder maxRetries(int n) {
      this.retryPolicy =
          new RetryPolicy(
              n, retryPolicy.initialBackoff(), retryPolicy.multiplier(), retryPolicy.maxBackoff());
      return this;
    }

    public Builder retryPolicy(RetryPolicy policy) {
      this.retryPolicy = policy;
      return this;
    }

    public Builder minDelayBetweenRequests(Duration d) {
      this.minDelayBetweenRequests = d;
      return this;
    }

    /**
     * Supply a pre-built {@link HttpClient}. Otherwise one is created with the configured connect
     * timeout.
     */
    public Builder httpClient(HttpClient client) {
      this.httpClient = client;
      return this;
    }

    /**
     * Override the base URI used to build search URLs. Intended for tests (e.g. pointing at a
     * {@code MockWebServer}). When set, URLs take the form {@code
     * <baseUriOverride>/_/<siteSubdomain>/search/...}.
     */
    public Builder baseUriOverride(URI base) {
      this.baseUriOverride = base;
      return this;
    }

    public Craigslist build() {
      HttpClient client =
          (httpClient != null)
              ? httpClient
              : HttpClient.newBuilder().connectTimeout(connectTimeout).build();
      HttpFetcher fetcher =
          new HttpFetcher(
              client,
              retryPolicy,
              new RateLimiter(minDelayBetweenRequests),
              userAgent,
              requestTimeout);
      UriBuilder uriBuilder = new UriBuilder(Optional.ofNullable(baseUriOverride));
      LOG.info(
          "initialized Craigslist client (ua=\"{}\", requestTimeout={}, maxRetries={},"
              + " minDelay={}, baseUriOverride={})",
          userAgent,
          requestTimeout,
          retryPolicy.maxAttempts(),
          minDelayBetweenRequests,
          baseUriOverride);
      return new Craigslist(client, fetcher, uriBuilder);
    }
  }
}
