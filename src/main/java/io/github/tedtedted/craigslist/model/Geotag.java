package io.github.tedtedted.craigslist.model;

/** A latitude/longitude pair scraped from a listing's detail page. */
public record Geotag(double latitude, double longitude) {

  public Geotag {
    if (latitude < -90.0 || latitude > 90.0) {
      throw new IllegalArgumentException("latitude out of range: " + latitude);
    }
    if (longitude < -180.0 || longitude > 180.0) {
      throw new IllegalArgumentException("longitude out of range: " + longitude);
    }
  }
}
