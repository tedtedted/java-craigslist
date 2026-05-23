package io.github.tedredington.craigslist.parser;

import io.github.tedredington.craigslist.exception.CraigslistParsingException;
import io.github.tedredington.craigslist.model.Listing;
import io.github.tedredington.craigslist.model.SearchPage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Glue between {@link ResultRowParser} and {@link io.github.tedredington.craigslist.model.SearchPage}.
 *  Tolerates individual malformed rows (skip + warn-log) but throws
 *  {@link CraigslistParsingException} when the page itself is unrecognizable. */
public final class PageParser {

    private static final Logger LOG = LoggerFactory.getLogger(PageParser.class);

    public static SearchPage parse(Document doc, ResultRowParser parser, int offset, URI sourceUri) {
        Elements rows = parser.selectRows(doc);
        if (rows.isEmpty() && !looksLikeEmptyResultsPage(doc)) {
            throw new CraigslistParsingException(
                    "could not locate any result rows on page", sourceUri, doc.html());
        }
        List<Listing> listings = new ArrayList<>(rows.size());
        for (Element row : rows) {
            try {
                listings.add(parser.parseRow(row));
            } catch (RuntimeException ex) {
                LOG.warn(
                        "skipping malformed row at {}: {} (row html={})",
                        sourceUri,
                        ex.getMessage(),
                        truncate(row.outerHtml()));
            }
        }
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
