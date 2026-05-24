package io.github.tedtedted.craigslist.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.tedtedted.craigslist.model.Listing;
import io.github.tedtedted.craigslist.model.SearchPage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

class ListingSpliteratorTest {

  private static Listing fakeListing(int n) {
    return Listing.builder()
        .id("id-" + n)
        .title("Listing " + n)
        .url(URI.create("https://example.com/" + n))
        .build();
  }

  private static Stream<Listing> stream(IntFunction<SearchPage> fetcher) {
    return StreamSupport.stream(new ListingSpliterator(fetcher), false);
  }

  @Test
  void exhaustsSinglePage() {
    IntFunction<SearchPage> fetcher =
        offset -> new SearchPage(List.of(fakeListing(1), fakeListing(2)), offset, 2, false);
    assertThat(stream(fetcher).toList()).extracting(Listing::id).containsExactly("id-1", "id-2");
  }

  @Test
  void walksMultiplePages() {
    AtomicInteger calls = new AtomicInteger(0);
    List<List<Integer>> pages = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7));
    IntFunction<SearchPage> fetcher =
        offset -> {
          int page = calls.getAndIncrement();
          List<Listing> rows =
              pages.get(page).stream().map(ListingSpliteratorTest::fakeListing).toList();
          boolean hasNext = page < pages.size() - 1;
          return new SearchPage(rows, offset, 7, hasNext);
        };
    assertThat(stream(fetcher).toList())
        .extracting(Listing::id)
        .containsExactly("id-1", "id-2", "id-3", "id-4", "id-5", "id-6", "id-7");
    assertThat(calls.get()).isEqualTo(3);
  }

  @Test
  void stopsOnEmptyPage() {
    AtomicInteger calls = new AtomicInteger(0);
    IntFunction<SearchPage> fetcher =
        offset -> {
          int page = calls.getAndIncrement();
          if (page == 0) {
            return new SearchPage(List.of(fakeListing(1)), offset, 100, true);
          }
          return new SearchPage(List.of(), offset, 100, false);
        };
    assertThat(stream(fetcher).toList()).hasSize(1);
  }

  @Test
  void respects3000ResultCap() {
    IntFunction<SearchPage> fetcher =
        offset -> {
          List<Listing> rows = new ArrayList<>(120);
          for (int i = 0; i < 120; i++) {
            rows.add(fakeListing(offset + i));
          }
          return new SearchPage(rows, offset, 5000, true);
        };
    assertThat(stream(fetcher).count()).isLessThanOrEqualTo(3000);
  }
}
