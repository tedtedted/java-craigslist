package io.github.tedredington.craigslist;

import io.github.tedredington.craigslist.exception.InvalidFilterException;
import io.github.tedredington.craigslist.filter.FilterKeys;
import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.CategoryCode;
import io.github.tedredington.craigslist.model.Condition;
import io.github.tedredington.craigslist.model.ForSaleCategory;
import io.github.tedredington.craigslist.model.Site;
import io.github.tedredington.craigslist.parser.DefaultResultRowParser;
import io.github.tedredington.craigslist.parser.ResultRowParser;
import java.util.List;

/** Search Craigslist for-sale listings (general goods, autos, electronics, …). */
public final class CraigslistForSale extends CraigslistBase {

  private static final ResultRowParser PARSER = new DefaultResultRowParser();

  private CraigslistForSale(
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

  public static final class Builder extends BaseBuilder<CraigslistForSale, Builder> {

    private ForSaleCategory category = ForSaleCategory.ALL_FOR_SALE;
    private Integer minPrice;
    private Integer maxPrice;
    private Condition[] conditions;
    private Boolean deliveryAvailable;
    private String make;

    private Builder(Craigslist client) {
      super(client);
    }

    public Builder category(ForSaleCategory c) {
      if (c == null) {
        throw new InvalidFilterException("category", null, "must be non-null");
      }
      this.category = c;
      return this;
    }

    public Builder minPrice(int dollars) {
      if (dollars < 0) {
        throw new InvalidFilterException("minPrice", dollars, "must be >= 0");
      }
      this.minPrice = dollars;
      return this;
    }

    public Builder maxPrice(int dollars) {
      if (dollars < 0) {
        throw new InvalidFilterException("maxPrice", dollars, "must be >= 0");
      }
      this.maxPrice = dollars;
      return this;
    }

    public Builder condition(Condition... values) {
      this.conditions = values;
      return this;
    }

    public Builder deliveryAvailable(boolean v) {
      this.deliveryAvailable = v;
      return this;
    }

    public Builder make(String makeOrModel) {
      this.make = makeOrModel;
      return this;
    }

    @Override
    public CraigslistForSale build() {
      validateBase();
      if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
        throw new InvalidFilterException(
            "price", minPrice + ".." + maxPrice, "minPrice must be <= maxPrice");
      }
      if (minPrice != null)
        params.add(new QueryParam(FilterKeys.MIN_PRICE, Integer.toString(minPrice)));
      if (maxPrice != null)
        params.add(new QueryParam(FilterKeys.MAX_PRICE, Integer.toString(maxPrice)));
      if (conditions != null) {
        for (Condition c : conditions) {
          params.add(new QueryParam(FilterKeys.CONDITION, c.urlValue()));
        }
      }
      if (Boolean.TRUE.equals(deliveryAvailable)) {
        params.add(new QueryParam(FilterKeys.DELIVERY_AVAILABLE, "1"));
      }
      if (make != null && !make.isBlank()) {
        params.add(new QueryParam(FilterKeys.AUTO_MAKE, make));
      }
      return new CraigslistForSale(client, site, area, category, List.copyOf(params));
    }
  }
}
