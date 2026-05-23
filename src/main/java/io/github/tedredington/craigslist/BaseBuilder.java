package io.github.tedredington.craigslist;

import io.github.tedredington.craigslist.exception.InvalidFilterException;
import io.github.tedredington.craigslist.filter.FilterKeys;
import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.Site;
import java.util.ArrayList;
import java.util.List;

/** Shared fluent base for the per-category query builders. The CRTP self-type
 *  ({@code B}) means each setter returns the concrete subclass builder, so
 *  chained calls continue to expose the subclass's category-specific setters.
 *
 *  @param <S> the concrete query class produced by this builder
 *  @param <B> the concrete builder class (used for fluent return-type) */
public abstract class BaseBuilder<S extends CraigslistBase, B extends BaseBuilder<S, B>> {

    protected final Craigslist client;
    protected Site site;
    protected Area area;
    protected final List<QueryParam> params = new ArrayList<>();

    protected BaseBuilder(Craigslist client) {
        if (client == null) {
            throw new IllegalArgumentException("client must be non-null");
        }
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    protected final B self() {
        return (B) this;
    }

    /** Required. */
    public B site(Site s) {
        this.site = s;
        return self();
    }

    /** Optional. If supplied, must belong to the selected {@link #site(Site)}. */
    public B area(Area a) {
        this.area = a;
        return self();
    }

    /** Free-text search query. */
    public B query(String q) {
        if (q != null && !q.isBlank()) {
            params.add(new QueryParam(FilterKeys.QUERY, q));
        }
        return self();
    }

    /** When {@code true}, search only post titles (not bodies). */
    public B searchTitlesOnly(boolean v) {
        if (v) {
            params.add(new QueryParam(FilterKeys.SEARCH_TITLES, "T"));
        }
        return self();
    }

    /** Limit to listings that include at least one image. */
    public B hasImage(boolean v) {
        if (v) {
            params.add(new QueryParam(FilterKeys.HAS_IMAGE, "1"));
        }
        return self();
    }

    /** Limit to listings posted today. */
    public B postedToday(boolean v) {
        if (v) {
            params.add(new QueryParam(FilterKeys.POSTED_TODAY, "1"));
        }
        return self();
    }

    /** Bundle duplicate listings into a single result. */
    public B bundleDuplicates(boolean v) {
        if (v) {
            params.add(new QueryParam(FilterKeys.BUNDLE_DUPLICATES, "1"));
        }
        return self();
    }

    /** Limit to listings within {@code miles} of the supplied ZIP code. */
    public B searchDistanceMiles(int miles) {
        if (miles < 0) {
            throw new InvalidFilterException("searchDistanceMiles", miles, "must be >= 0");
        }
        params.add(new QueryParam(FilterKeys.SEARCH_DISTANCE, String.valueOf(miles)));
        return self();
    }

    /** Center point for {@link #searchDistanceMiles(int)}. */
    public B zipCode(String zip) {
        if (zip != null && !zip.isBlank()) {
            params.add(new QueryParam(FilterKeys.ZIP_CODE, zip.trim()));
        }
        return self();
    }

    /** Validates common state. Subclasses should call this from their {@link #build()} method. */
    protected final void validateBase() {
        if (site == null) {
            throw new InvalidFilterException("site", null, "site is required");
        }
        if (area != null && area.site() != site) {
            throw new InvalidFilterException(
                    "area",
                    area,
                    "area " + area + " belongs to " + area.site() + ", not " + site);
        }
    }

    /** Builds the concrete query. */
    public abstract S build();
}
