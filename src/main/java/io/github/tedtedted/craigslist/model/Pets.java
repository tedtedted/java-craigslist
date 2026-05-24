package io.github.tedtedted.craigslist.model;

/** Pet-friendliness filter for housing listings. */
public enum Pets {
  CATS_OK("pets_cat", "1"),
  DOGS_OK("pets_dog", "1");

  private final String urlKey;
  private final String urlValue;

  Pets(String urlKey, String urlValue) {
    this.urlKey = urlKey;
    this.urlValue = urlValue;
  }

  public String urlKey() {
    return urlKey;
  }

  public String urlValue() {
    return urlValue;
  }
}
