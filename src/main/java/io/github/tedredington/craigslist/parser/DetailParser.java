package io.github.tedredington.craigslist.parser;

import io.github.tedredington.craigslist.model.Geotag;
import io.github.tedredington.craigslist.model.Listing;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Parses a Craigslist detail page (single posting) into a {@link Listing} with body and geotag
 * populated.
 */
public final class DetailParser {

  public static Listing enrich(Listing base, Document detail) {
    Listing.Builder b = base.toBuilder();

    Element body = detail.selectFirst("#postingbody");
    if (body != null) {
      for (Element notice : body.select(".print-information, .print-qrcode-container")) {
        notice.remove();
      }
      b.body(body.text().trim());
    }

    Element map = detail.selectFirst("#map[data-latitude][data-longitude]");
    if (map != null) {
      try {
        double lat = Double.parseDouble(map.attr("data-latitude"));
        double lon = Double.parseDouble(map.attr("data-longitude"));
        b.geotag(new Geotag(lat, lon));
      } catch (IllegalArgumentException ignore) {
        // skip
      }
    }

    Elements attrs = detail.select(".attrgroup .attr, .attrgroup span");
    if (!attrs.isEmpty()) {
      Map<String, String> extras = new LinkedHashMap<>(base.customFields());
      for (Element attr : attrs) {
        String text = attr.text().trim();
        if (text.isEmpty()) continue;
        int colon = text.indexOf(':');
        if (colon > 0 && colon < text.length() - 1) {
          extras.put(
              text.substring(0, colon).trim().toLowerCase(), text.substring(colon + 1).trim());
        }
      }
      if (!extras.isEmpty()) {
        b.customFields(extras);
      }
    }

    return b.build();
  }

  private DetailParser() {}
}
