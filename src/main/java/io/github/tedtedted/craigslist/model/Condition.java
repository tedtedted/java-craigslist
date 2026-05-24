package io.github.tedtedted.craigslist.model;

/** Condition filter for for-sale listings. */
public enum Condition {
  NEW("10"),
  LIKE_NEW("20"),
  EXCELLENT("30"),
  GOOD("40"),
  FAIR("50"),
  SALVAGE("60");

  private final String urlValue;

  Condition(String urlValue) {
    this.urlValue = urlValue;
  }

  public String urlValue() {
    return urlValue;
  }
}
