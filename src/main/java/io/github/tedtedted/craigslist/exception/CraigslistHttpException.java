package io.github.tedtedted.craigslist.exception;

import java.net.URI;

/**
 * Thrown when Craigslist returned a non-2xx HTTP status after retries were exhausted. Carries the
 * status code, the URI, a truncated response-body snippet for diagnostics, and the number of
 * attempts made.
 */
public class CraigslistHttpException extends CraigslistTransportException {

  private static final int SNIPPET_MAX_CHARS = 512;

  private final int statusCode;
  private final String responseBodySnippet;
  private final int attemptCount;

  public CraigslistHttpException(int statusCode, URI uri, String responseBody, int attemptCount) {
    super("HTTP " + statusCode + " from " + uri + " (attempts=" + attemptCount + ")", uri);
    this.statusCode = statusCode;
    this.responseBodySnippet = truncate(responseBody);
    this.attemptCount = attemptCount;
  }

  private static String truncate(String body) {
    if (body == null) {
      return "";
    }
    if (body.length() <= SNIPPET_MAX_CHARS) {
      return body;
    }
    return body.substring(0, SNIPPET_MAX_CHARS) + "…";
  }

  public int statusCode() {
    return statusCode;
  }

  public String responseBodySnippet() {
    return responseBodySnippet;
  }

  public int attemptCount() {
    return attemptCount;
  }
}
