package io.github.tedtedted.craigslist.model;

public enum GigsCategory implements CategoryCode {
  ALL_GIGS("ggg", "all gigs"),
  COMPUTER("cpg", "computer gigs"),
  CREATIVE("crg", "creative gigs"),
  CREW("cwg", "crew gigs"),
  DOMESTIC("dmg", "domestic gigs"),
  EVENT("evg", "event gigs"),
  LABOR("lbg", "labor gigs"),
  TALENT("tlg", "talent gigs"),
  WRITING("wrg", "writing gigs");

  private final String code;
  private final String displayName;

  GigsCategory(String code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String displayName() {
    return displayName;
  }
}
