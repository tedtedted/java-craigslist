package io.github.tedredington.craigslist.exception;

/**
 * Root of the {@code java-craigslist} exception hierarchy. All exceptions thrown by the library
 * extend this type and are unchecked, so they propagate cleanly through {@link
 * java.util.stream.Stream} pipelines and lambdas.
 */
public class CraigslistException extends RuntimeException {

  public CraigslistException(String message) {
    super(message);
  }

  public CraigslistException(String message, Throwable cause) {
    super(message, cause);
  }
}
