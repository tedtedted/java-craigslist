package io.github.tedredington.craigslist.exception;

import java.net.URI;

/** Thrown when the request exceeded the configured timeout. The underlying
 *  {@link java.net.http.HttpTimeoutException} is preserved as the cause. */
public class CraigslistTimeoutException extends CraigslistTransportException {

    public CraigslistTimeoutException(String message, URI uri, Throwable cause) {
        super(message, uri, cause);
    }
}
