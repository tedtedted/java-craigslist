package io.github.tedtedted.craigslist.exception;

import java.net.URI;

/**
 * Thrown when Craigslist responded with HTTP 404, typically meaning the site/area/category
 * combination doesn't correspond to a real listings page.
 */
public class CraigslistNotFoundException extends CraigslistHttpException {

  public CraigslistNotFoundException(URI uri, String responseBody, int attemptCount) {
    super(404, uri, responseBody, attemptCount);
  }
}
