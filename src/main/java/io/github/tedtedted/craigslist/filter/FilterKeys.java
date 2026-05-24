package io.github.tedtedted.craigslist.filter;

/**
 * Craigslist URL query-parameter keys used to express search filters. These match the URL
 * parameters the live Craigslist search endpoint accepts.
 */
public final class FilterKeys {

  // Base filters (apply to all categories)
  public static final String QUERY = "query";
  public static final String SEARCH_TITLES = "srchType";
  public static final String HAS_IMAGE = "hasPic";
  public static final String POSTED_TODAY = "postedToday";
  public static final String BUNDLE_DUPLICATES = "bundleDuplicates";
  public static final String SEARCH_DISTANCE = "search_distance";
  public static final String ZIP_CODE = "postal";

  // Housing
  public static final String MIN_PRICE = "min_price";
  public static final String MAX_PRICE = "max_price";
  public static final String MIN_BEDROOMS = "min_bedrooms";
  public static final String MAX_BEDROOMS = "max_bedrooms";
  public static final String MIN_BATHROOMS = "min_bathrooms";
  public static final String MAX_BATHROOMS = "max_bathrooms";
  public static final String MIN_SQFT = "minSqft";
  public static final String MAX_SQFT = "maxSqft";
  public static final String PRIVATE_ROOM = "private_room";
  public static final String PRIVATE_BATH = "private_bath";
  public static final String FURNISHED = "is_furnished";
  public static final String NO_SMOKING = "no_smoking";
  public static final String WHEELCHAIR_ACCESSIBLE = "wheelchair_acccess";
  public static final String LAUNDRY = "laundry";
  public static final String PARKING = "parking";

  // For sale
  public static final String CONDITION = "condition";
  public static final String DELIVERY_AVAILABLE = "delivery_available";
  public static final String AUTO_MAKE = "auto_make_model";

  // Jobs / Resumes
  public static final String EMPLOYMENT_TYPE = "employment_type";
  public static final String INTERNSHIP = "is_internship";
  public static final String TELECOMMUTE = "is_telecommuting";

  // Gigs
  public static final String IS_PAID = "is_paid";

  // Pagination
  public static final String OFFSET = "s";

  private FilterKeys() {}
}
