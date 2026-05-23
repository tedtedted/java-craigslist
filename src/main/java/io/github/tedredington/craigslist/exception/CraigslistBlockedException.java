package io.github.tedredington.craigslist.exception;

import java.net.URI;

/** Thrown when Craigslist responded with HTTP 403 (Forbidden). Most commonly this means
 *  the caller has been flagged as a scraper. Rotate user-agent, lower request rate, or
 *  back off entirely for a while. */
public class CraigslistBlockedException extends CraigslistHttpException {

    public CraigslistBlockedException(URI uri, String responseBody, int attemptCount) {
        super(403, uri, responseBody, attemptCount);
    }
}
