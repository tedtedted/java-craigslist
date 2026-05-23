package io.github.tedredington.craigslist.http;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.tedredington.craigslist.filter.FilterKeys;
import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.HousingCategory;
import io.github.tedredington.craigslist.model.Site;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class UriBuilderTest {

  private final UriBuilder ub = new UriBuilder(Optional.empty());

  @Test
  void buildsBareSiteUrl() {
    URI uri =
        ub.buildSearch(Site.SF_BAY, Optional.empty(), HousingCategory.ALL_HOUSING, List.of(), 0);
    assertThat(uri.toString()).isEqualTo("https://sfbay.craigslist.org/search/hhh");
  }

  @Test
  void includesAreaPathSegment() {
    URI uri =
        ub.buildSearch(
            Site.SF_BAY, Optional.of(Area.SFC), HousingCategory.APARTMENTS, List.of(), 0);
    assertThat(uri.toString()).isEqualTo("https://sfbay.craigslist.org/search/sfc/apa");
  }

  @Test
  void encodesParamsAndOffset() {
    URI uri =
        ub.buildSearch(
            Site.SF_BAY,
            Optional.of(Area.SFC),
            HousingCategory.APARTMENTS,
            List.of(
                new QueryParam(FilterKeys.QUERY, "two bedroom"),
                new QueryParam(FilterKeys.MIN_PRICE, "1500")),
            120);
    assertThat(uri.toString())
        .isEqualTo(
            "https://sfbay.craigslist.org/search/sfc/apa"
                + "?query=two+bedroom&min_price=1500&s=120");
  }

  @Test
  void honorsBaseUriOverride() {
    UriBuilder overridden = new UriBuilder(Optional.of(URI.create("http://localhost:1234")));
    URI uri =
        overridden.buildSearch(
            Site.SF_BAY, Optional.of(Area.SFC), HousingCategory.APARTMENTS, List.of(), 0);
    assertThat(uri.toString()).isEqualTo("http://localhost:1234/_/sfbay/search/sfc/apa");
  }
}
