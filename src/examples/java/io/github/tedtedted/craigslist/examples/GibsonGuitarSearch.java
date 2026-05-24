package io.github.tedtedted.craigslist.examples;

import io.github.tedtedted.craigslist.Craigslist;
import io.github.tedtedted.craigslist.CraigslistForSale;
import io.github.tedtedted.craigslist.model.ForSaleCategory;
import io.github.tedtedted.craigslist.model.Listing;
import io.github.tedtedted.craigslist.model.Site;

/**
 * Search Craigslist Chicago for Gibson guitars in the musical-instruments category. Mirrors
 * https://chicago.craigslist.org/search/msa?query=gibson
 */
public final class GibsonGuitarSearch {

  public static void main(String[] args) {
    try (Craigslist cl = Craigslist.create()) {
      CraigslistForSale search =
          CraigslistForSale.builder(cl)
              .site(Site.CHICAGO)
              // .area(Area.CHI_CITY)
              .category(ForSaleCategory.MUSIC_INSTRUMENTS)
              .query("gibson")
              .hasImage(true)
              .minPrice(800)
              .maxPrice(8000)
              .build();

      System.out.println("URL: " + search.uriForOffset(0));
      System.out.println("Approx total: " + search.approximateCount());
      System.out.println();
      search.stream().limit(25).forEach(GibsonGuitarSearch::print);
    }
  }

  private static void print(Listing l) {
    System.out.printf(
        "%-8s | %-50s | %s%n",
        l.priceCents().isPresent() ? "$" + l.priceCents().getAsInt() / 100 : "?",
        truncate(l.title(), 50),
        l.url());
  }

  private static String truncate(String s, int max) {
    return s.length() <= max ? s : s.substring(0, max - 1) + "…";
  }

  private GibsonGuitarSearch() {}
}
