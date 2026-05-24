package io.github.tedtedted.craigslist.examples;

import io.github.tedtedted.craigslist.Craigslist;
import io.github.tedtedted.craigslist.CraigslistForSale;
import io.github.tedtedted.craigslist.model.ForSaleCategory;
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
      search.stream().limit(25).forEach(System.out::println);
    }
  }

  private GibsonGuitarSearch() {}
}
