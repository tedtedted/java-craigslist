package io.github.tedtedted.craigslist.exception;

import java.net.URI;
import java.util.Optional;

/**
 * Base type for any network or HTTP failure. Catch this if you want a single catch-all for
 * transport problems without discriminating between timeouts, connection errors, and HTTP status
 * errors.
 */
public class CraigslistTransportException extends CraigslistException {

  private final URI uri;

  public CraigslistTransportException(String message, URI uri) {
    super(message);
    this.uri = uri;
  }

  public CraigslistTransportException(String message, URI uri, Throwable cause) {
    super(message, cause);
    this.uri = uri;
  }

  public Optional<URI> uri() {
    return Optional.ofNullable(uri);
  }
}
