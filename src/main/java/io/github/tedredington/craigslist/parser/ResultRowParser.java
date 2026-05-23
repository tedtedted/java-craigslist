package io.github.tedredington.craigslist.parser;

import io.github.tedredington.craigslist.model.Listing;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Strategy for parsing a Craigslist search-results page. Each category may
 *  override {@link #parseRow(Element)} to add category-specific extras. */
public interface ResultRowParser {

    /** Selects all listing-row elements on the page. */
    Elements selectRows(Document doc);

    /** Parses a single row element into a {@link Listing}. */
    Listing parseRow(Element row);

    /** Parses the overall result count (used by {@code approximateCount}). */
    int parseTotalCount(Document doc);
}
