package io.github.tedtedted.craigslist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.tedtedted.craigslist.exception.InvalidFilterException;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.HousingCategory;
import io.github.tedtedted.craigslist.model.Site;
import org.junit.jupiter.api.Test;

class CraigslistHousingBuilderTest {

  private final Craigslist client = Craigslist.create();

  @Test
  void requiresSite() {
    assertThatThrownBy(() -> CraigslistHousing.builder(client).build())
        .isInstanceOf(InvalidFilterException.class)
        .hasMessageContaining("site");
  }

  @Test
  void rejectsAreaFromWrongSite() {
    assertThatThrownBy(
            () -> CraigslistHousing.builder(client).site(Site.SF_BAY).area(Area.MANHATTAN).build())
        .isInstanceOf(InvalidFilterException.class)
        .hasMessageContaining("area");
  }

  @Test
  void rejectsMinPriceGreaterThanMax() {
    assertThatThrownBy(
            () ->
                CraigslistHousing.builder(client)
                    .site(Site.SF_BAY)
                    .minPrice(5000)
                    .maxPrice(1000)
                    .build())
        .isInstanceOf(InvalidFilterException.class)
        .hasMessageContaining("price");
  }

  @Test
  void rejectsNegativePrice() {
    assertThatThrownBy(() -> CraigslistHousing.builder(client).site(Site.SF_BAY).minPrice(-1))
        .isInstanceOf(InvalidFilterException.class);
  }

  @Test
  void buildsWithOnlyMinPrice() {
    CraigslistHousing q =
        CraigslistHousing.builder(client).site(Site.SF_BAY).minPrice(1500).build();
    assertThat(q.uriForOffset(0).toString())
        .contains("min_price=1500")
        .doesNotContain("max_price=");
  }

  @Test
  void buildsWithOnlyMaxPrice() {
    CraigslistHousing q =
        CraigslistHousing.builder(client).site(Site.SF_BAY).maxPrice(3000).build();
    assertThat(q.uriForOffset(0).toString())
        .contains("max_price=3000")
        .doesNotContain("min_price=");
  }

  @Test
  void buildsHappyPath() {
    CraigslistHousing q =
        CraigslistHousing.builder(client)
            .site(Site.SF_BAY)
            .area(Area.SFC)
            .category(HousingCategory.APARTMENTS)
            .query("studio")
            .minPrice(1500)
            .maxPrice(3000)
            .hasImage(true)
            .build();
    assertThat(q.site()).isEqualTo(Site.SF_BAY);
    assertThat(q.area()).contains(Area.SFC);
    assertThat(q.category()).isEqualTo(HousingCategory.APARTMENTS);
    String url = q.uriForOffset(0).toString();
    assertThat(url)
        .contains("sfbay.craigslist.org/search/sfc/apa")
        .contains("query=studio")
        .contains("min_price=1500")
        .contains("max_price=3000")
        .contains("hasPic=1");
  }

  @Test
  void buildCanBeCalledRepeatedlyWithoutDuplicatingParams() {
    CraigslistHousing.Builder builder =
        CraigslistHousing.builder(client)
            .site(Site.SF_BAY)
            .query("studio")
            .minPrice(1500)
            .maxPrice(3000)
            .hasImage(true);

    CraigslistHousing first = builder.build();
    CraigslistHousing second = builder.build();

    assertThat(second.uriForOffset(0)).isEqualTo(first.uriForOffset(0));
    String url = second.uriForOffset(0).toString();
    assertThat(occurrences(url, "query=studio")).isEqualTo(1);
    assertThat(occurrences(url, "min_price=1500")).isEqualTo(1);
    assertThat(occurrences(url, "max_price=3000")).isEqualTo(1);
    assertThat(occurrences(url, "hasPic=1")).isEqualTo(1);
  }

  private static int occurrences(String value, String needle) {
    int count = 0;
    int index = 0;
    while ((index = value.indexOf(needle, index)) >= 0) {
      count++;
      index += needle.length();
    }
    return count;
  }
}
