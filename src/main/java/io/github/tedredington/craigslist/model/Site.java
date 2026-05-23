package io.github.tedredington.craigslist.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A Craigslist regional site (subdomain). This is a curated initial set covering the most-used
 * metropolitan areas. The full ~450-site list is intended to be generated and committed via the
 * {@code generateSiteEnums} build task; see {@code tools/codegen/README.md}.
 */
public enum Site {
  // United States — California
  SF_BAY("sfbay", "SF Bay Area", Region.US_CALIFORNIA),
  LOS_ANGELES("losangeles", "Los Angeles", Region.US_CALIFORNIA),
  SAN_DIEGO("sandiego", "San Diego", Region.US_CALIFORNIA),
  SACRAMENTO("sacramento", "Sacramento", Region.US_CALIFORNIA),
  ORANGE_COUNTY("orangecounty", "Orange County", Region.US_CALIFORNIA),
  INLAND_EMPIRE("inlandempire", "Inland Empire", Region.US_CALIFORNIA),
  FRESNO("fresno", "Fresno / Madera", Region.US_CALIFORNIA),
  BAKERSFIELD("bakersfield", "Bakersfield", Region.US_CALIFORNIA),
  SANTA_BARBARA("santabarbara", "Santa Barbara", Region.US_CALIFORNIA),
  VENTURA("ventura", "Ventura County", Region.US_CALIFORNIA),

  // United States — Washington / Oregon
  SEATTLE("seattle", "Seattle / Tacoma", Region.US_WASHINGTON),
  PORTLAND("portland", "Portland", Region.US_OREGON),

  // United States — Texas
  AUSTIN("austin", "Austin", Region.US_TEXAS),
  DALLAS("dallas", "Dallas / Fort Worth", Region.US_TEXAS),
  HOUSTON("houston", "Houston", Region.US_TEXAS),
  SAN_ANTONIO("sanantonio", "San Antonio", Region.US_TEXAS),

  // United States — Northeast
  NEW_YORK("newyork", "New York City", Region.US_NEW_YORK),
  BOSTON("boston", "Boston", Region.US_MASSACHUSETTS),
  PHILADELPHIA("philadelphia", "Philadelphia", Region.US_PENNSYLVANIA),
  PITTSBURGH("pittsburgh", "Pittsburgh", Region.US_PENNSYLVANIA),
  WASHINGTON_DC("washingtondc", "Washington, DC", Region.US_DC),
  BALTIMORE("baltimore", "Baltimore", Region.US_MARYLAND),

  // United States — Midwest
  CHICAGO("chicago", "Chicago", Region.US_ILLINOIS),
  DETROIT("detroit", "Detroit Metro", Region.US_MICHIGAN),
  MINNEAPOLIS("minneapolis", "Minneapolis / St Paul", Region.US_MINNESOTA),
  MILWAUKEE("milwaukee", "Milwaukee", Region.US_WISCONSIN),
  INDIANAPOLIS("indianapolis", "Indianapolis", Region.US_INDIANA),
  COLUMBUS("columbus", "Columbus", Region.US_OHIO),
  CINCINNATI("cincinnati", "Cincinnati", Region.US_OHIO),
  CLEVELAND("cleveland", "Cleveland", Region.US_OHIO),
  KANSAS_CITY("kansascity", "Kansas City", Region.US_MISSOURI),
  ST_LOUIS("stlouis", "St Louis", Region.US_MISSOURI),

  // United States — South / Southeast
  ATLANTA("atlanta", "Atlanta", Region.US_GEORGIA),
  MIAMI("miami", "South Florida", Region.US_FLORIDA),
  ORLANDO("orlando", "Orlando", Region.US_FLORIDA),
  TAMPA("tampa", "Tampa Bay Area", Region.US_FLORIDA),
  JACKSONVILLE("jacksonville", "Jacksonville", Region.US_FLORIDA),
  CHARLOTTE("charlotte", "Charlotte", Region.US_NORTH_CAROLINA),
  RALEIGH("raleigh", "Raleigh / Durham", Region.US_NORTH_CAROLINA),
  NASHVILLE("nashville", "Nashville", Region.US_TENNESSEE),
  MEMPHIS("memphis", "Memphis", Region.US_TENNESSEE),
  NEW_ORLEANS("neworleans", "New Orleans", Region.US_LOUISIANA),

  // United States — Mountain / Southwest
  DENVER("denver", "Denver", Region.US_COLORADO),
  PHOENIX("phoenix", "Phoenix", Region.US_ARIZONA),
  LAS_VEGAS("lasvegas", "Las Vegas", Region.US_NEVADA),
  SALT_LAKE_CITY("saltlakecity", "Salt Lake City", Region.US_UTAH),
  ALBUQUERQUE("albuquerque", "Albuquerque", Region.US_NEW_MEXICO),

  // United States — Hawaii / Alaska
  HONOLULU("honolulu", "Honolulu", Region.US_HAWAII),
  ANCHORAGE("anchorage", "Anchorage / Mat-Su", Region.US_ALASKA),

  // Canada
  TORONTO("toronto", "Toronto", Region.CANADA),
  VANCOUVER("vancouver", "Vancouver, BC", Region.CANADA),
  MONTREAL("montreal", "Montreal", Region.CANADA),
  CALGARY("calgary", "Calgary", Region.CANADA),
  OTTAWA("ottawa", "Ottawa", Region.CANADA),

  // International
  LONDON("london", "London, UK", Region.EUROPE),
  PARIS("paris", "Paris, France", Region.EUROPE),
  BERLIN("berlin", "Berlin", Region.EUROPE),
  AMSTERDAM("amsterdam", "Amsterdam", Region.EUROPE),
  MADRID("madrid", "Madrid", Region.EUROPE),
  SYDNEY("sydney", "Sydney", Region.OCEANIA),
  MELBOURNE("melbourne", "Melbourne", Region.OCEANIA),
  TOKYO("tokyo", "Tokyo", Region.ASIA),
  SINGAPORE("singapore", "Singapore", Region.ASIA),
  MEXICO_CITY("mexicocity", "Mexico City", Region.MEXICO);

  private final String subdomain;
  private final String displayName;
  private final Region region;

  Site(String subdomain, String displayName, Region region) {
    this.subdomain = subdomain;
    this.displayName = displayName;
    this.region = region;
  }

  /**
   * The DNS subdomain used to address this site (e.g. {@code "sfbay"} for {@code
   * sfbay.craigslist.org}).
   */
  public String subdomain() {
    return subdomain;
  }

  public String displayName() {
    return displayName;
  }

  public Region region() {
    return region;
  }

  /** Look up a site by its Craigslist subdomain string. */
  public static Optional<Site> byCode(String subdomain) {
    if (subdomain == null) {
      return Optional.empty();
    }
    return Arrays.stream(values()).filter(s -> s.subdomain.equals(subdomain)).findFirst();
  }

  /** All sites within the given region. */
  public static List<Site> inRegion(Region region) {
    return Stream.of(values()).filter(s -> s.region == region).toList();
  }
}
