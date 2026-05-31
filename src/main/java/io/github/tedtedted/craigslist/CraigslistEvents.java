package io.github.tedtedted.craigslist;

import io.github.tedtedted.craigslist.exception.InvalidFilterException;
import io.github.tedtedted.craigslist.filter.QueryParam;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.CategoryCode;
import io.github.tedtedted.craigslist.model.EventsCategory;
import io.github.tedtedted.craigslist.model.Site;
import io.github.tedtedted.craigslist.parser.DefaultResultRowParser;
import io.github.tedtedted.craigslist.parser.ResultRowParser;
import java.util.List;

/** Search Craigslist events listings. */
public final class CraigslistEvents extends CraigslistBase {

  private static final ResultRowParser PARSER = new DefaultResultRowParser();

  private CraigslistEvents(
      Craigslist client, Site site, Area area, CategoryCode category, List<QueryParam> params) {
    super(client, site, area, category, params);
  }

  @Override
  protected ResultRowParser parser() {
    return PARSER;
  }

  public static Builder builder(Craigslist client) {
    return new Builder(client);
  }

  public static final class Builder extends BaseBuilder<CraigslistEvents, Builder> {

    private EventsCategory category = EventsCategory.ALL_EVENTS;

    private Builder(Craigslist client) {
      super(client);
    }

    public Builder category(EventsCategory c) {
      if (c == null) {
        throw new InvalidFilterException("category", null, "must be non-null");
      }
      this.category = c;
      return this;
    }

    @Override
    public CraigslistEvents build() {
      validateBase();
      return new CraigslistEvents(client, site, area, category, List.copyOf(baseParams()));
    }
  }
}
