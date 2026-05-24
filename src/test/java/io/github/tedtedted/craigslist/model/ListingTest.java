package io.github.tedtedted.craigslist.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;

class ListingTest {

  @Test
  void toStringUnwrapsPresentOptionalsAndIncludesRequiredFields() {
    Listing l =
        Listing.builder()
            .id("1")
            .title("Sunny 1BR")
            .url(URI.create("https://example.com/1"))
            .priceCents(240_000)
            .location("Mission District")
            .hasImage(true)
            .build();
    // Optionals are unwrapped (no "Optional[...]" wrapper); empty fields are omitted.
    assertThat(l.toString())
        .isEqualTo(
            "Listing[id=1, title=Sunny 1BR, url=https://example.com/1,"
                + " priceCents=240000, location=Mission District, hasImage=true]");
  }

  @Test
  void toStringOmitsEmptyOptionalsAndEmptyCustomFields() {
    Listing l =
        Listing.builder()
            .id("2")
            .title("Some Post")
            .url(URI.create("https://example.com/2"))
            .build();
    // datetimePosted, priceCents, location, geotag, body, customFields all empty → omitted.
    assertThat(l.toString())
        .isEqualTo("Listing[id=2, title=Some Post, url=https://example.com/2, hasImage=false]");
  }
}
