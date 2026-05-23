package io.github.tedredington.craigslist.filter;

/**
 * A single key/value pair for the Craigslist search URL. Builders accumulate these and the URI
 * builder emits them as percent-encoded query-string fragments.
 */
public record QueryParam(String key, String value) {

  public QueryParam {
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("key must be non-blank");
    }
    if (value == null) {
      throw new IllegalArgumentException("value must be non-null");
    }
  }
}
