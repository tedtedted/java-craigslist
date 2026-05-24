package io.github.tedtedted.craigslist.parser;

import io.github.tedtedted.craigslist.model.Listing;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default search-row parser. Tries modern Craigslist selectors first ({@code li.cl-search-result},
 * {@code li.cl-static-search-result}) and falls back to the legacy {@code li.result-row} layout.
 */
public class DefaultResultRowParser implements ResultRowParser {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultResultRowParser.class);

  private static final String MODERN_ROW_SELECTOR =
      "li.cl-search-result, li.cl-static-search-result";
  private static final String LEGACY_ROW_SELECTOR = "li.result-row";
  private static final Pattern PRICE_PATTERN = Pattern.compile("\\$?([\\d,]+)");
  // Extracts the listing id from a posting URL path like
  // .../d/san-francisco-foo-bar/7936350978.html → "7936350978".
  private static final Pattern ID_FROM_URL_PATTERN = Pattern.compile("/(\\d+)\\.html");

  @Override
  public Elements selectRows(Document doc) {
    Elements rows = doc.select(MODERN_ROW_SELECTOR);
    if (rows.isEmpty()) {
      rows = doc.select(LEGACY_ROW_SELECTOR);
      if (!rows.isEmpty()) {
        LOG.warn("modern selector found no rows — using legacy {}", LEGACY_ROW_SELECTOR);
      }
    }
    return rows;
  }

  @Override
  public Listing parseRow(Element row) {
    Listing.Builder b = Listing.builder();

    Element titleAnchor =
        firstNonNull(
            row.selectFirst("a.posting-title"),
            row.selectFirst(".titlestring"),
            row.selectFirst("a.cl-app-anchor"),
            row.selectFirst("a.result-title"),
            row.selectFirst("a"));
    String href = "";
    if (titleAnchor != null) {
      // Prefer the post title (e.g. <span class="label">) over the anchor's full text,
      // which on the modern static layout swallows the surrounding meta.
      String anchorText = titleAnchor.text().trim();
      Element titleLabel =
          firstNonNull(titleAnchor.selectFirst("span.label"), titleAnchor.selectFirst(".title"));
      if (titleLabel != null && !titleLabel.text().isBlank()) {
        anchorText = titleLabel.text().trim();
      } else if (anchorText.isEmpty() || anchorText.equals(row.attr("title"))) {
        // fall back to the row's title attribute when the anchor has no visible text
        anchorText = row.attr("title").trim();
      }
      b.title(anchorText);
      href = titleAnchor.attr("abs:href");
      if (href.isEmpty()) {
        href = titleAnchor.attr("href");
      }
      if (!href.isEmpty()) {
        try {
          b.url(URI.create(href));
        } catch (IllegalArgumentException ignore) {
          // best-effort
        }
      }
    }
    // The modern static layout doesn't set data-pid on the <li>; derive id from the URL
    // (e.g. .../7936350978.html → "7936350978").
    String id = firstNonEmpty(row.attr("data-pid"), row.id(), idFromUrl(href));
    b.id(id);

    Element timeEl = row.selectFirst("time[datetime]");
    if (timeEl != null) {
      String dt = timeEl.attr("datetime");
      try {
        b.datetimePosted(OffsetDateTime.parse(dt));
      } catch (DateTimeParseException ignore) {
        // some pages emit local date-times without offset; leave empty
      }
    }

    Element priceEl =
        firstNonNull(
            row.selectFirst(".priceinfo"),
            row.selectFirst(".result-price"),
            row.selectFirst(".price"));
    if (priceEl != null) {
      Matcher m = PRICE_PATTERN.matcher(priceEl.text());
      if (m.find()) {
        String digits = m.group(1).replace(",", "");
        try {
          long dollars = Long.parseLong(digits);
          b.priceCents((int) Math.min(dollars * 100L, Integer.MAX_VALUE));
        } catch (NumberFormatException ignore) {
          // skip
        }
      }
    }

    Element locEl =
        firstNonNull(
            row.selectFirst(".location"),
            row.selectFirst(".result-hood"),
            row.selectFirst(".meta .location"));
    if (locEl != null) {
      String loc = locEl.text().trim();
      if (loc.startsWith("(") && loc.endsWith(")")) {
        loc = loc.substring(1, loc.length() - 1);
      }
      if (!loc.isEmpty()) {
        b.location(loc);
      }
    }

    boolean hasImage = !row.select("img, .gallery, .swipe").isEmpty();
    b.hasImage(hasImage);

    return b.build();
  }

  @Override
  public int parseTotalCount(Document doc) {
    Element t =
        firstNonNull(
            doc.selectFirst("span.totalcount"),
            doc.selectFirst(".cl-results-count"),
            doc.selectFirst(".totalcount"));
    if (t == null) {
      return -1;
    }
    Matcher m = Pattern.compile("([\\d,]+)").matcher(t.text());
    if (m.find()) {
      try {
        return Integer.parseInt(m.group(1).replace(",", ""));
      } catch (NumberFormatException ignore) {
        return -1;
      }
    }
    return -1;
  }

  private static String idFromUrl(String url) {
    if (url == null || url.isEmpty()) {
      return "";
    }
    Matcher m = ID_FROM_URL_PATTERN.matcher(url);
    return m.find() ? m.group(1) : "";
  }

  protected static Element firstNonNull(Element... candidates) {
    for (Element c : candidates) {
      if (c != null) {
        return c;
      }
    }
    return null;
  }

  private static String firstNonEmpty(String... candidates) {
    for (String c : candidates) {
      if (c != null && !c.isBlank()) {
        return c;
      }
    }
    return "";
  }
}
