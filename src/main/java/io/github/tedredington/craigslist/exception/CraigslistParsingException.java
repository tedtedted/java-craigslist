package io.github.tedredington.craigslist.exception;

import java.net.URI;
import java.util.Optional;

/** Thrown when the HTML returned by Craigslist could not be parsed at the page level
 *  (no result-row elements and no "no results" marker, or {@code totalcount} unparseable).
 *  Individual malformed rows are skipped and warn-logged, not thrown. */
public class CraigslistParsingException extends CraigslistException {

    private static final int SNIPPET_MAX_CHARS = 512;

    private final URI sourceUri;
    private final String snippet;

    public CraigslistParsingException(String message, URI sourceUri, String html) {
        super(message + " (uri=" + sourceUri + ")");
        this.sourceUri = sourceUri;
        this.snippet = truncate(html);
    }

    public CraigslistParsingException(
            String message, URI sourceUri, String html, Throwable cause) {
        super(message + " (uri=" + sourceUri + ")", cause);
        this.sourceUri = sourceUri;
        this.snippet = truncate(html);
    }

    private static String truncate(String html) {
        if (html == null) {
            return "";
        }
        if (html.length() <= SNIPPET_MAX_CHARS) {
            return html;
        }
        return html.substring(0, SNIPPET_MAX_CHARS) + "…";
    }

    public Optional<URI> sourceUri() {
        return Optional.ofNullable(sourceUri);
    }

    public String snippet() {
        return snippet;
    }
}
