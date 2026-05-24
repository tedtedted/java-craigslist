package io.github.tedtedted.craigslist.parser;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.tedtedted.craigslist.model.Listing;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

class HousingResultRowParserTest {

  private final HousingResultRowParser parser = new HousingResultRowParser();

  @Test
  void parsesBedroomsAndSqftFromHousingBlurb() throws IOException {
    Document doc =
        Jsoup.parse(
            Files.readString(Path.of("src/test/resources/fixtures/housing/page-0.html")),
            "https://sfbay.craigslist.org/search/sfc/apa");
    var rows = parser.selectRows(doc);
    assertThat(rows).hasSize(3);

    Listing l0 = parser.parseRow(rows.get(0));
    assertThat(l0.customFields()).containsEntry("bedrooms", "1").containsEntry("sqft", "650");

    Listing l1 = parser.parseRow(rows.get(1));
    assertThat(l1.customFields()).containsEntry("bedrooms", "0").containsEntry("sqft", "420");

    Listing l2 = parser.parseRow(rows.get(2));
    assertThat(l2.customFields()).containsEntry("bedrooms", "2").containsEntry("sqft", "1100");
  }
}
