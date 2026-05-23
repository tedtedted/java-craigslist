package io.github.tedredington.craigslist.model;

import java.util.List;

/** A single page of search results, as parsed from one HTTP response. */
public record SearchPage(List<Listing> listings, int offset, int totalCount, boolean hasNext) {

    public SearchPage {
        if (listings == null) {
            throw new IllegalArgumentException("listings must be non-null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be >= 0");
        }
        if (totalCount < 0) {
            throw new IllegalArgumentException("totalCount must be >= 0");
        }
        listings = List.copyOf(listings);
    }
}
