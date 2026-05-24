package io.github.tedtedted.craigslist.parser;

import io.github.tedtedted.craigslist.model.Listing;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;

/** Adds housing-specific extras to each row: bedrooms, sqft, available-on date. */
public final class HousingResultRowParser extends DefaultResultRowParser {

  private static final Pattern BEDROOMS_PATTERN = Pattern.compile("(\\d+)\\s*br");
  private static final Pattern SQFT_PATTERN = Pattern.compile("(\\d+)\\s*ft2");

  @Override
  public Listing parseRow(Element row) {
    Listing base = super.parseRow(row);
    Element housing =
        firstNonNull(row.selectFirst(".housing"), row.selectFirst(".result-meta .housing"));
    if (housing == null) {
      return base;
    }
    String text = housing.text();
    Map<String, String> extras = new LinkedHashMap<>(base.customFields());
    Matcher br = BEDROOMS_PATTERN.matcher(text);
    if (br.find()) {
      extras.put("bedrooms", br.group(1));
    }
    Matcher sqft = SQFT_PATTERN.matcher(text);
    if (sqft.find()) {
      extras.put("sqft", sqft.group(1));
    }
    if (extras.isEmpty()) {
      return base;
    }
    return base.toBuilder().customFields(extras).build();
  }
}
