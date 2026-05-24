package io.github.tedtedted.craigslist.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;

class ListingTest {

  @Test
  void toStringRendersPriceTitleLocationUrl() {
    Listing l =
        Listing.builder()
            .id("1")
            .title("Sunny 1BR")
            .url(URI.create("https://example.com/1"))
            .priceCents(240_000)
            .location("Mission District")
            .build();
    assertThat(l.toString())
        .isEqualTo("$2400 — Sunny 1BR (Mission District) — https://example.com/1");
  }

  @Test
  void toStringElidesMissingPriceAndLocation() {
    Listing l =
        Listing.builder()
            .id("2")
            .title("Some Post")
            .url(URI.create("https://example.com/2"))
            .build();
    assertThat(l.toString()).isEqualTo("Some Post — https://example.com/2");
  }
}
