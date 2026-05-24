package io.github.tedtedted.craigslist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.tedtedted.craigslist.exception.CraigslistBlockedException;
import io.github.tedtedted.craigslist.exception.CraigslistRateLimitException;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.HousingCategory;
import io.github.tedtedted.craigslist.model.Listing;
import io.github.tedtedted.craigslist.model.Site;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** End-to-end tests of {@link CraigslistHousing} backed by {@link MockWebServer}. */
class CraigslistHousingIntegrationTest {

  private MockWebServer server;
  private Craigslist client;

  @BeforeEach
  void setUp() throws IOException {
    server = new MockWebServer();
    server.start();
    client =
        Craigslist.builder()
            .baseUriOverride(URI.create(server.url("/").toString()))
            .minDelayBetweenRequests(Duration.ZERO)
            .build();
  }

  @AfterEach
  void tearDown() throws IOException {
    client.close();
    server.shutdown();
  }

  private static String fixture(String path) throws IOException {
    return Files.readString(Path.of("src/test/resources/fixtures", path));
  }

  @Test
  void iteratesListingsFromMockServer() throws IOException {
    server.enqueue(new MockResponse().setBody(fixture("housing/page-0.html")));
    server.enqueue(new MockResponse().setBody(fixture("housing/page-empty.html")));

    CraigslistHousing housing =
        CraigslistHousing.builder(client)
            .site(Site.SF_BAY)
            .area(Area.SFC)
            .category(HousingCategory.APARTMENTS)
            .build();

    List<Listing> listings = housing.toList();
    assertThat(listings).hasSize(3);
    assertThat(listings)
        .extracting(Listing::title)
        .containsExactly(
            "Sunny 1BR in Mission", "Cozy Studio Downtown", "Spacious 2BR with Parking");
  }

  @Test
  void emptyResultsProduceEmptyIterableNotException() throws IOException {
    // toList() and first() each trigger one fetch (streams are single-use)
    server.enqueue(new MockResponse().setBody(fixture("housing/page-empty.html")));
    server.enqueue(new MockResponse().setBody(fixture("housing/page-empty.html")));
    CraigslistHousing housing = CraigslistHousing.builder(client).site(Site.SF_BAY).build();
    assertThat(housing.toList()).isEmpty();
    assertThat(housing.first()).isEmpty();
  }

  @Test
  void approximateCountReturnsTotalcount() throws IOException {
    server.enqueue(new MockResponse().setBody(fixture("housing/page-0.html")));
    CraigslistHousing housing = CraigslistHousing.builder(client).site(Site.SF_BAY).build();
    assertThat(housing.approximateCount()).isEqualTo(42);
  }

  @Test
  void mapsHttp403ToBlockedException() {
    server.enqueue(new MockResponse().setResponseCode(403).setBody("blocked"));
    CraigslistHousing housing = CraigslistHousing.builder(client).site(Site.SF_BAY).build();
    assertThatThrownBy(housing::toList).isInstanceOf(CraigslistBlockedException.class);
  }

  @Test
  void retriesOn500ThenSucceeds() throws IOException {
    server.enqueue(new MockResponse().setResponseCode(500));
    server.enqueue(new MockResponse().setBody(fixture("housing/page-0.html")));
    server.enqueue(new MockResponse().setBody(fixture("housing/page-empty.html")));
    CraigslistHousing housing = CraigslistHousing.builder(client).site(Site.SF_BAY).build();
    assertThat(housing.toList()).hasSize(3);
  }

  @Test
  void mapsHttp429ToRateLimitAfterRetries() {
    for (int i = 0; i < 5; i++) {
      server.enqueue(new MockResponse().setResponseCode(429));
    }
    CraigslistHousing housing = CraigslistHousing.builder(client).site(Site.SF_BAY).build();
    assertThatThrownBy(housing::toList).isInstanceOf(CraigslistRateLimitException.class);
  }
}
