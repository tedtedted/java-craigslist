package io.github.tedtedted.craigslist.exception;

/**
 * Thrown when the user supplied invalid configuration to the library (bad filters, missing required
 * fields, mismatched site/area, etc.).
 */
public class CraigslistConfigException extends CraigslistException {

  public CraigslistConfigException(String message) {
    super(message);
  }

  public CraigslistConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
