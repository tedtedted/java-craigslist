package io.github.tedtedted.craigslist.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A sub-area within a {@link Site} (e.g. {@code Area.SFC} = San Francisco proper inside {@code
 * Site.SF_BAY}). Curated initial set; full list is intended to be generated and committed via the
 * {@code generateSiteEnums} task.
 */
public enum Area {
  // SF Bay
  SFC("sfc", "San Francisco", Site.SF_BAY),
  EBY("eby", "East Bay", Site.SF_BAY),
  SBY("sby", "South Bay", Site.SF_BAY),
  NBY("nby", "North Bay", Site.SF_BAY),
  PEN("pen", "Peninsula", Site.SF_BAY),
  SCZ("scz", "Santa Cruz", Site.SF_BAY),

  // New York
  MANHATTAN("mnh", "Manhattan", Site.NEW_YORK),
  BROOKLYN("brk", "Brooklyn", Site.NEW_YORK),
  BRONX("brx", "Bronx", Site.NEW_YORK),
  QUEENS("que", "Queens", Site.NEW_YORK),
  STATEN_ISLAND("stn", "Staten Island", Site.NEW_YORK),
  JERSEY_SHORE("jsy", "Jersey Shore", Site.NEW_YORK),
  LONG_ISLAND("lgi", "Long Island", Site.NEW_YORK),
  WESTCHESTER("wch", "Westchester", Site.NEW_YORK),
  FAIRFIELD_CT("fct", "Fairfield County", Site.NEW_YORK),

  // Los Angeles
  LA_CENTRAL("lac", "Central LA", Site.LOS_ANGELES),
  LA_WESTSIDE("wst", "Westside-Southbay-310", Site.LOS_ANGELES),
  LA_SAN_GABRIEL("sgv", "San Gabriel Valley", Site.LOS_ANGELES),
  LA_SAN_FERNANDO("sfv", "San Fernando Valley", Site.LOS_ANGELES),
  LA_LONG_BEACH("lgb", "Long Beach / 562", Site.LOS_ANGELES),
  LA_ANTELOPE("ant", "Antelope Valley", Site.LOS_ANGELES),

  // Chicago
  CHI_CITY("chc", "City of Chicago", Site.CHICAGO),
  CHI_NORTH("nch", "North Chicago", Site.CHICAGO),
  CHI_SOUTH("sox", "South Chicago", Site.CHICAGO),
  CHI_WEST("wcl", "West Chicago", Site.CHICAGO),
  CHI_NORTHWEST_IND("nwi", "Northwest Indiana", Site.CHICAGO),

  // Washington DC
  DC_CITY("doc", "City of DC", Site.WASHINGTON_DC),
  DC_NORTHERN_VA("nva", "Northern Virginia", Site.WASHINGTON_DC),
  DC_MARYLAND("mld", "Maryland (DC area)", Site.WASHINGTON_DC),

  // Seattle
  SEA_CITY("see", "Seattle", Site.SEATTLE),
  SEA_EASTSIDE("est", "Eastside", Site.SEATTLE),
  SEA_TACOMA("tac", "Tacoma / Pierce", Site.SEATTLE),
  SEA_SNOHOMISH("snoh", "Snohomish County", Site.SEATTLE),
  SEA_KITSAP("kit", "Kitsap / West Puget", Site.SEATTLE);

  private final String code;
  private final String displayName;
  private final Site site;

  Area(String code, String displayName, Site site) {
    this.code = code;
    this.displayName = displayName;
    this.site = site;
  }

  /** URL path segment for this area (e.g. {@code "sfc"}). */
  public String code() {
    return code;
  }

  public String displayName() {
    return displayName;
  }

  /** The parent site this area belongs to. */
  public Site site() {
    return site;
  }

  /** All areas defined for the given site. */
  public static List<Area> forSite(Site site) {
    return Arrays.stream(values()).filter(a -> a.site == site).toList();
  }

  public static Optional<Area> byCode(Site site, String code) {
    return Arrays.stream(values()).filter(a -> a.site == site && a.code.equals(code)).findFirst();
  }
}
