package io.github.tedtedted.craigslist.model;

/**
 * Coarse geographic grouping for {@link Site} values. Useful for iterating every site in a state or
 * country (e.g. {@code Site.inRegion(Region.US_CALIFORNIA)}).
 */
public enum Region {
  US_ALABAMA("US-AL", "Alabama", "United States"),
  US_ALASKA("US-AK", "Alaska", "United States"),
  US_ARIZONA("US-AZ", "Arizona", "United States"),
  US_ARKANSAS("US-AR", "Arkansas", "United States"),
  US_CALIFORNIA("US-CA", "California", "United States"),
  US_COLORADO("US-CO", "Colorado", "United States"),
  US_CONNECTICUT("US-CT", "Connecticut", "United States"),
  US_DELAWARE("US-DE", "Delaware", "United States"),
  US_DC("US-DC", "District of Columbia", "United States"),
  US_FLORIDA("US-FL", "Florida", "United States"),
  US_GEORGIA("US-GA", "Georgia", "United States"),
  US_HAWAII("US-HI", "Hawaii", "United States"),
  US_IDAHO("US-ID", "Idaho", "United States"),
  US_ILLINOIS("US-IL", "Illinois", "United States"),
  US_INDIANA("US-IN", "Indiana", "United States"),
  US_IOWA("US-IA", "Iowa", "United States"),
  US_KANSAS("US-KS", "Kansas", "United States"),
  US_KENTUCKY("US-KY", "Kentucky", "United States"),
  US_LOUISIANA("US-LA", "Louisiana", "United States"),
  US_MAINE("US-ME", "Maine", "United States"),
  US_MARYLAND("US-MD", "Maryland", "United States"),
  US_MASSACHUSETTS("US-MA", "Massachusetts", "United States"),
  US_MICHIGAN("US-MI", "Michigan", "United States"),
  US_MINNESOTA("US-MN", "Minnesota", "United States"),
  US_MISSISSIPPI("US-MS", "Mississippi", "United States"),
  US_MISSOURI("US-MO", "Missouri", "United States"),
  US_MONTANA("US-MT", "Montana", "United States"),
  US_NEBRASKA("US-NE", "Nebraska", "United States"),
  US_NEVADA("US-NV", "Nevada", "United States"),
  US_NEW_HAMPSHIRE("US-NH", "New Hampshire", "United States"),
  US_NEW_JERSEY("US-NJ", "New Jersey", "United States"),
  US_NEW_MEXICO("US-NM", "New Mexico", "United States"),
  US_NEW_YORK("US-NY", "New York", "United States"),
  US_NORTH_CAROLINA("US-NC", "North Carolina", "United States"),
  US_NORTH_DAKOTA("US-ND", "North Dakota", "United States"),
  US_OHIO("US-OH", "Ohio", "United States"),
  US_OKLAHOMA("US-OK", "Oklahoma", "United States"),
  US_OREGON("US-OR", "Oregon", "United States"),
  US_PENNSYLVANIA("US-PA", "Pennsylvania", "United States"),
  US_RHODE_ISLAND("US-RI", "Rhode Island", "United States"),
  US_SOUTH_CAROLINA("US-SC", "South Carolina", "United States"),
  US_SOUTH_DAKOTA("US-SD", "South Dakota", "United States"),
  US_TENNESSEE("US-TN", "Tennessee", "United States"),
  US_TEXAS("US-TX", "Texas", "United States"),
  US_UTAH("US-UT", "Utah", "United States"),
  US_VERMONT("US-VT", "Vermont", "United States"),
  US_VIRGINIA("US-VA", "Virginia", "United States"),
  US_WASHINGTON("US-WA", "Washington", "United States"),
  US_WEST_VIRGINIA("US-WV", "West Virginia", "United States"),
  US_WISCONSIN("US-WI", "Wisconsin", "United States"),
  US_WYOMING("US-WY", "Wyoming", "United States"),
  CANADA("CA", "Canada", "Canada"),
  MEXICO("MX", "Mexico", "Mexico"),
  EUROPE("EU", "Europe", "Europe"),
  ASIA("AS", "Asia", "Asia"),
  OCEANIA("OC", "Oceania", "Oceania"),
  AFRICA("AF", "Africa", "Africa"),
  SOUTH_AMERICA("SA", "South America", "South America"),
  CENTRAL_AMERICA("CAM", "Central America", "Central America"),
  CARIBBEAN("CR", "Caribbean", "Caribbean"),
  MIDDLE_EAST("ME", "Middle East", "Middle East");

  private final String code;
  private final String displayName;
  private final String country;

  Region(String code, String displayName, String country) {
    this.code = code;
    this.displayName = displayName;
    this.country = country;
  }

  public String code() {
    return code;
  }

  public String displayName() {
    return displayName;
  }

  public String country() {
    return country;
  }
}
