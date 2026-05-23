package io.github.tedredington.craigslist.parser;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.tedredington.craigslist.model.Listing;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

class DefaultResultRowParserTest {

  private final DefaultResultRowParser parser = new DefaultResultRowParser();

  private Document loadFixture(String path) throws IOException {
    Path p = Path.of("src/test/resources/fixtures", path);
    return Jsoup.parse(Files.readString(p), "https://sfbay.craigslist.org/search/sfc/apa");
  }

  @Test
  void parsesAllRowsInModernFixture() throws Exception {
    Document doc = loadFixture("housing/page-0.html");
    List<Listing> listings = parser.selectRows(doc).stream().map(parser::parseRow).toList();
    assertThat(listings).hasSize(3);
    Listing first = listings.get(0);
    assertThat(first.id()).isEqualTo("7700000001");
    assertThat(first.title()).isEqualTo("Sunny 1BR in Mission");
    assertThat(first.priceCents()).hasValue(240_000);
    assertThat(first.location()).contains("Mission District");
    assertThat(first.hasImage()).isTrue();
    assertThat(first.datetimePosted()).isPresent();
  }

  @Test
  void parsesTotalCount() throws Exception {
    Document doc = loadFixture("housing/page-0.html");
    assertThat(parser.parseTotalCount(doc)).isEqualTo(42);
  }

  @Test
  void detectsNoImageWhenAbsent() throws Exception {
    Document doc = loadFixture("housing/page-0.html");
    List<Listing> listings = parser.selectRows(doc).stream().map(parser::parseRow).toList();
    assertThat(listings.get(1).hasImage()).isFalse();
  }
}
