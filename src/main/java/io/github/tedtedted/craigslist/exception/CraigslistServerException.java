package io.github.tedtedted.craigslist.exception;

import java.net.URI;

/** Thrown when Craigslist responded with a 5xx status after retries were exhausted. */
public class CraigslistServerException extends CraigslistHttpException {

  public CraigslistServerException(int statusCode, URI uri, String responseBody, int attemptCount) {
    super(statusCode, uri, responseBody, attemptCount);
  }
}
