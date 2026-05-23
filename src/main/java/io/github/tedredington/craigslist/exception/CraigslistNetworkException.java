package io.github.tedredington.craigslist.exception;

import java.net.URI;

/**
 * Thrown when the underlying transport fails before an HTTP response was received (DNS resolution,
 * connection refused, socket I/O, etc.). The original {@link java.io.IOException} is preserved as
 * the cause.
 */
public class CraigslistNetworkException extends CraigslistTransportException {

  public CraigslistNetworkException(String message, URI uri, Throwable cause) {
    super(message, uri, cause);
  }
}
