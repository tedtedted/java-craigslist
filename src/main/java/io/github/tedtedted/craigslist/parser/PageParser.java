package io.github.tedtedted.craigslist.parser;

import io.github.tedtedted.craigslist.exception.CraigslistParsingException;
import io.github.tedtedted.craigslist.model.Listing;
import io.github.tedtedted.craigslist.model.SearchPage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Glue between {@link ResultRowParser} and {@link io.github.tedtedted.craigslist.model.SearchPage}.
 * Tolerates individual malformed rows (skip + warn-log) but throws {@link
 * CraigslistParsingException} when the page itself is unrecognizable.
 */
public final class PageParser {

  private static final Logger LOG = LoggerFactory.getLogger(PageParser.class);

  public static SearchPage parse(Document doc, ResultRowParser parser, int offset, URI sourceUri) {
    Elements rows = parser.selectRows(doc);
    if (rows.isEmpty() && !looksLikeEmptyResultsPage(doc)) {
      throw new CraigslistParsingException(
          "could not locate any result rows on page", sourceUri, doc.html());
    }
    List<Listing> listings = new ArrayList<>(rows.size());
    int skipped = 0;
    for (Element row : rows) {
      try {
        listings.add(parser.parseRow(row));
      } catch (RuntimeException ex) {
        skipped++;
        // Per-row failures are common when the DOM has minor variations; log at DEBUG so
        // they don't flood normal output. The summary below catches widespread failures.
        LOG.debug(
            "skipping malformed row at {}: {} (row html={})",
            sourceUri,
            ex.getMessage(),
            truncate(row.outerHtml()));
      }
    }
    if (skipped > 0) {
      double skipRatio = (double) skipped / rows.size();
      // If we're losing more than half the rows on a page, the DOM has likely shifted —
      // emit one WARN per page so operators notice without spamming per-row.
      if (skipRatio > 0.5) {
        LOG.warn(
            "skipped {}/{} rows ({}%) at {} — Craigslist DOM may have changed",
            skipped, rows.size(), Math.round(skipRatio * 100), sourceUri);
      } else {
        LOG.debug("skipped {}/{} rows at {}", skipped, rows.size(), sourceUri);
      }
    }
    LOG.debug("parsed {} listings from {}", listings.size(), sourceUri);
    int total = parser.parseTotalCount(doc);
    if (total < 0) {
      total = offset + listings.size();
    }
    boolean hasNext = !listings.isEmpty() && (offset + listings.size()) < total;
    return new SearchPage(listings, offset, total, hasNext);
  }

  private static boolean looksLikeEmptyResultsPage(Document doc) {
    return doc.selectFirst(".no-results, .noresults, .cl-no-results") != null
        || doc.text().toLowerCase().contains("no results");
  }

  private static String truncate(String s) {
    return s.length() <= 200 ? s : s.substring(0, 200) + "…";
  }

  private PageParser() {}
}
