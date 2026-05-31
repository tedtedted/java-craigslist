package io.github.tedtedted.craigslist;

import io.github.tedtedted.craigslist.exception.InvalidFilterException;
import io.github.tedtedted.craigslist.filter.QueryParam;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.CategoryCode;
import io.github.tedtedted.craigslist.model.CommunityCategory;
import io.github.tedtedted.craigslist.model.Site;
import io.github.tedtedted.craigslist.parser.DefaultResultRowParser;
import io.github.tedtedted.craigslist.parser.ResultRowParser;
import java.util.List;

/** Search Craigslist community listings. */
public final class CraigslistCommunity extends CraigslistBase {

  private static final ResultRowParser PARSER = new DefaultResultRowParser();

  private CraigslistCommunity(
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

  public static final class Builder extends BaseBuilder<CraigslistCommunity, Builder> {

    private CommunityCategory category = CommunityCategory.ALL_COMMUNITY;

    private Builder(Craigslist client) {
      super(client);
    }

    public Builder category(CommunityCategory c) {
      if (c == null) {
        throw new InvalidFilterException("category", null, "must be non-null");
      }
      this.category = c;
      return this;
    }

    @Override
    public CraigslistCommunity build() {
      validateBase();
      return new CraigslistCommunity(client, site, area, category, List.copyOf(baseParams()));
    }
  }
}
