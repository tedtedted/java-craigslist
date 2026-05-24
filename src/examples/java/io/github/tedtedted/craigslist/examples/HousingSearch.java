package io.github.tedtedted.craigslist.examples;

import io.github.tedtedted.craigslist.Craigslist;
import io.github.tedtedted.craigslist.CraigslistHousing;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.HousingCategory;
import io.github.tedtedted.craigslist.model.Listing;
import io.github.tedtedted.craigslist.model.Site;

/** Prints the first 20 SF apartment listings between $1500 and $3000. */
public final class HousingSearch {

  public static void main(String[] args) {
    try (Craigslist cl = Craigslist.create()) {
      CraigslistHousing housing =
          CraigslistHousing.builder(cl)
              .site(Site.SF_BAY)
              .area(Area.SFC)
              .category(HousingCategory.APARTMENTS)
              .minPrice(1500)
              .maxPrice(3000)
              .hasImage(true)
              .build();

      System.out.println("Approx total: " + housing.approximateCount());
      housing.stream().limit(20).forEach(HousingSearch::print);
    }
  }

  private static void print(Listing l) {
    System.out.printf(
        "%-8s | %-40s | %s%n",
        l.priceCents().isPresent() ? "$" + l.priceCents().getAsInt() / 100 : "?",
        l.title(),
        l.url());
  }

  private HousingSearch() {}
}
