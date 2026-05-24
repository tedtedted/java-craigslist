package io.github.tedtedted.craigslist.model;

public enum Laundry {
  W_D_IN_UNIT("1"),
  W_D_HOOKUPS("2"),
  LAUNDRY_IN_BUILDING("3"),
  LAUNDRY_ON_SITE("4"),
  NO_LAUNDRY_ON_SITE("5");

  private final String urlValue;

  Laundry(String urlValue) {
    this.urlValue = urlValue;
  }

  public String urlValue() {
    return urlValue;
  }
}
