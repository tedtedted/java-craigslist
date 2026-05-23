package io.github.tedredington.craigslist.exception;

/**
 * Thrown when a filter value fails validation (range violation, area/site mismatch, missing
 * required field, etc.). Carries the offending field name and value so callers can build
 * user-facing error messages without parsing strings.
 */
public class InvalidFilterException extends CraigslistConfigException {

  private final String filterName;
  private final Object value;
  private final String reason;

  public InvalidFilterException(String filterName, Object value, String reason) {
    super("invalid filter '" + filterName + "' (value=" + value + "): " + reason);
    this.filterName = filterName;
    this.value = value;
    this.reason = reason;
  }

  public String filterName() {
    return filterName;
  }

  public Object value() {
    return value;
  }

  public String reason() {
    return reason;
  }
}
