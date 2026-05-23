package io.github.tedredington.craigslist;

import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.CategoryCode;
import io.github.tedredington.craigslist.model.Listing;
import io.github.tedredington.craigslist.model.SearchPage;
import io.github.tedredington.craigslist.model.Site;
import io.github.tedredington.craigslist.pagination.ListingSpliterator;
import io.github.tedredington.craigslist.parser.DetailParser;
import io.github.tedredington.craigslist.parser.PageParser;
import io.github.tedredington.craigslist.parser.ResultRowParser;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jsoup.nodes.Document;

/**
 * Parent of every category query class. Each subclass implements {@link Iterable
 * Iterable&lt;Listing&gt;} with transparent auto-pagination; the query itself is the result.
 *
 * <p>Subclasses are sealed — there is one for each Craigslist category (Community, Events, ForSale,
 * Gigs, Housing, Jobs, Resumes, Services).
 */
public abstract sealed class CraigslistBase implements Iterable<Listing>
    permits CraigslistCommunity,
        CraigslistEvents,
        CraigslistForSale,
        CraigslistGigs,
        CraigslistHousing,
        CraigslistJobs,
        CraigslistResumes,
        CraigslistServices {

  private final Craigslist client;
  private final Site site;
  private final Optional<Area> area;
  private final CategoryCode category;
  private final List<QueryParam> params;
  private final AtomicLong cachedTotalCount = new AtomicLong(-1L);

  protected CraigslistBase(
      Craigslist client, Site site, Area area, CategoryCode category, List<QueryParam> params) {
    this.client = client;
    this.site = site;
    this.area = Optional.ofNullable(area);
    this.category = category;
    this.params = params;
  }

  /** Subclasses supply their category-specific row parser. */
  protected abstract ResultRowParser parser();

  public Site site() {
    return site;
  }

  public Optional<Area> area() {
    return area;
  }

  public CategoryCode category() {
    return category;
  }

  /** Returns the URI for a specific result page (offset = 0 is the first page). */
  public URI uriForOffset(int offset) {
    return client.uriBuilder().buildSearch(site, area, category, params, offset);
  }

  @Override
  public final Iterator<Listing> iterator() {
    return Spliterators.iterator(spliterator());
  }

  @Override
  public final Spliterator<Listing> spliterator() {
    return new ListingSpliterator(this::fetchPage);
  }

  /** Lazy, auto-paginated stream of listings (capped at Craigslist's 3000-result limit). */
  public final Stream<Listing> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  /** Convenience: returns the first listing, if any. Empty when the query has zero results. */
  public final Optional<Listing> first() {
    return stream().findFirst();
  }

  /**
   * Convenience: collects all listings into a {@code List}. Be mindful of the 3000-cap and
   * Craigslist's rate limits before calling this on a broad query.
   */
  public final List<Listing> toList() {
    return stream().toList();
  }

  /**
   * The approximate total count Craigslist reports on the first results page. Returns 0 when there
   * are no results. Memoized per query instance.
   */
  public final long approximateCount() {
    long cached = cachedTotalCount.get();
    if (cached >= 0) {
      return cached;
    }
    SearchPage page = fetchPage(0);
    long total = Math.max(page.totalCount(), 0);
    cachedTotalCount.set(total);
    return total;
  }

  /**
   * Fetches and parses the listing's detail page, returning a new {@link Listing} with body text
   * and (when available) geotag populated.
   */
  public final Listing fetchDetail(Listing listing) {
    Document doc = client.fetcher().getDocument(listing.url());
    return DetailParser.enrich(listing, doc);
  }

  private SearchPage fetchPage(int offset) {
    URI uri = uriForOffset(offset);
    Document doc = client.fetcher().getDocument(uri);
    return PageParser.parse(doc, parser(), offset, uri);
  }
}
