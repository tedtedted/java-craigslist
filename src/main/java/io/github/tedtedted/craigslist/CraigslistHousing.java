package io.github.tedtedted.craigslist;

import io.github.tedtedted.craigslist.exception.InvalidFilterException;
import io.github.tedtedted.craigslist.filter.FilterKeys;
import io.github.tedtedted.craigslist.filter.QueryParam;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.CategoryCode;
import io.github.tedtedted.craigslist.model.HousingCategory;
import io.github.tedtedted.craigslist.model.Laundry;
import io.github.tedtedted.craigslist.model.Parking;
import io.github.tedtedted.craigslist.model.Pets;
import io.github.tedtedted.craigslist.model.Site;
import io.github.tedtedted.craigslist.parser.HousingResultRowParser;
import io.github.tedtedted.craigslist.parser.ResultRowParser;
import java.util.List;

/**
 * Search Craigslist housing listings (apartments, rooms, sublets, real estate, …). Each instance is
 * immutable and reusable. Iterate it to retrieve listings.
 */
public final class CraigslistHousing extends CraigslistBase {

  private static final ResultRowParser PARSER = new HousingResultRowParser();

  private CraigslistHousing(
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

  /**
   * Builder for housing queries. Exposes housing-specific filters in addition to the base setters
   * inherited from {@link BaseBuilder}.
   */
  public static final class Builder extends BaseBuilder<CraigslistHousing, Builder> {

    private HousingCategory category = HousingCategory.ALL_HOUSING;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer minBedrooms;
    private Integer maxBedrooms;
    private Integer minBathrooms;
    private Integer maxBathrooms;
    private Integer minSqft;
    private Integer maxSqft;
    private Boolean privateRoom;
    private Boolean privateBath;
    private Boolean furnished;
    private Boolean noSmoking;
    private Boolean wheelchairAccessible;
    private Pets[] pets;
    private Laundry[] laundry;
    private Parking[] parking;

    private Builder(Craigslist client) {
      super(client);
    }

    public Builder category(HousingCategory c) {
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

    public Builder minBedrooms(int n) {
      if (n < 0) {
        throw new InvalidFilterException("minBedrooms", n, "must be >= 0");
      }
      this.minBedrooms = n;
      return this;
    }

    public Builder maxBedrooms(int n) {
      if (n < 0) {
        throw new InvalidFilterException("maxBedrooms", n, "must be >= 0");
      }
      this.maxBedrooms = n;
      return this;
    }

    public Builder minBathrooms(int n) {
      if (n < 0) {
        throw new InvalidFilterException("minBathrooms", n, "must be >= 0");
      }
      this.minBathrooms = n;
      return this;
    }

    public Builder maxBathrooms(int n) {
      if (n < 0) {
        throw new InvalidFilterException("maxBathrooms", n, "must be >= 0");
      }
      this.maxBathrooms = n;
      return this;
    }

    public Builder minSqft(int sqft) {
      if (sqft < 0) {
        throw new InvalidFilterException("minSqft", sqft, "must be >= 0");
      }
      this.minSqft = sqft;
      return this;
    }

    public Builder maxSqft(int sqft) {
      if (sqft < 0) {
        throw new InvalidFilterException("maxSqft", sqft, "must be >= 0");
      }
      this.maxSqft = sqft;
      return this;
    }

    public Builder privateRoom(boolean v) {
      this.privateRoom = v;
      return this;
    }

    public Builder privateBath(boolean v) {
      this.privateBath = v;
      return this;
    }

    public Builder furnished(boolean v) {
      this.furnished = v;
      return this;
    }

    public Builder noSmoking(boolean v) {
      this.noSmoking = v;
      return this;
    }

    public Builder wheelchairAccessible(boolean v) {
      this.wheelchairAccessible = v;
      return this;
    }

    public Builder pets(Pets... allowed) {
      this.pets = allowed;
      return this;
    }

    public Builder laundry(Laundry... opts) {
      this.laundry = opts;
      return this;
    }

    public Builder parking(Parking... opts) {
      this.parking = opts;
      return this;
    }

    @Override
    public CraigslistHousing build() {
      validateBase();
      validateRange("price", minPrice, maxPrice);
      validateRange("bedrooms", minBedrooms, maxBedrooms);
      validateRange("bathrooms", minBathrooms, maxBathrooms);
      validateRange("sqft", minSqft, maxSqft);

      if (minPrice != null) params.add(qp(FilterKeys.MIN_PRICE, minPrice));
      if (maxPrice != null) params.add(qp(FilterKeys.MAX_PRICE, maxPrice));
      if (minBedrooms != null) params.add(qp(FilterKeys.MIN_BEDROOMS, minBedrooms));
      if (maxBedrooms != null) params.add(qp(FilterKeys.MAX_BEDROOMS, maxBedrooms));
      if (minBathrooms != null) params.add(qp(FilterKeys.MIN_BATHROOMS, minBathrooms));
      if (maxBathrooms != null) params.add(qp(FilterKeys.MAX_BATHROOMS, maxBathrooms));
      if (minSqft != null) params.add(qp(FilterKeys.MIN_SQFT, minSqft));
      if (maxSqft != null) params.add(qp(FilterKeys.MAX_SQFT, maxSqft));
      if (Boolean.TRUE.equals(privateRoom))
        params.add(new QueryParam(FilterKeys.PRIVATE_ROOM, "1"));
      if (Boolean.TRUE.equals(privateBath))
        params.add(new QueryParam(FilterKeys.PRIVATE_BATH, "1"));
      if (Boolean.TRUE.equals(furnished)) params.add(new QueryParam(FilterKeys.FURNISHED, "1"));
      if (Boolean.TRUE.equals(noSmoking)) params.add(new QueryParam(FilterKeys.NO_SMOKING, "1"));
      if (Boolean.TRUE.equals(wheelchairAccessible))
        params.add(new QueryParam(FilterKeys.WHEELCHAIR_ACCESSIBLE, "1"));
      if (pets != null) {
        for (Pets p : pets) {
          params.add(new QueryParam(p.urlKey(), p.urlValue()));
        }
      }
      if (laundry != null) {
        for (Laundry l : laundry) {
          params.add(new QueryParam(FilterKeys.LAUNDRY, l.urlValue()));
        }
      }
      if (parking != null) {
        for (Parking p : parking) {
          params.add(new QueryParam(FilterKeys.PARKING, p.urlValue()));
        }
      }
      return new CraigslistHousing(client, site, area, category, List.copyOf(params));
    }

    private static void validateRange(String name, Integer min, Integer max) {
      if (min != null && max != null && min > max) {
        throw new InvalidFilterException(name, min + ".." + max, "min must be <= max");
      }
    }

    private static QueryParam qp(String key, int value) {
      return new QueryParam(key, Integer.toString(value));
    }
  }
}
