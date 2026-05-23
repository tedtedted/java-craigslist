package io.github.tedredington.craigslist.http;

import io.github.tedredington.craigslist.filter.FilterKeys;
import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.CategoryCode;
import io.github.tedredington.craigslist.model.Site;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/** Assembles Craigslist search URLs of the form
 *  {@code https://{site}.craigslist.org/search/{area?}/{category}?...&s=offset}.
 *  Tests can supply a base URI override (e.g. {@code http://localhost:8080}) to
 *  target a {@code MockWebServer} instead of the live site. */
public final class UriBuilder {

    private final Optional<URI> baseOverride;

    public UriBuilder(Optional<URI> baseOverride) {
        this.baseOverride = baseOverride == null ? Optional.empty() : baseOverride;
    }

    public URI buildSearch(
            Site site,
            Optional<Area> area,
            CategoryCode category,
            List<QueryParam> params,
            int offset) {

        StringBuilder url = new StringBuilder();
        url.append(siteBase(site));
        url.append("/search");
        area.ifPresent(a -> url.append('/').append(a.code()));
        url.append('/').append(category.code());

        boolean first = true;
        for (QueryParam p : params) {
            url.append(first ? '?' : '&');
            first = false;
            url.append(encode(p.key())).append('=').append(encode(p.value()));
        }
        if (offset > 0) {
            url.append(first ? '?' : '&');
            url.append(FilterKeys.OFFSET).append('=').append(offset);
        }
        return URI.create(url.toString());
    }

    public URI buildAbout() {
        return URI.create(rootBase() + "/about/sites");
    }

    public URI resolveDetail(Site site, String pathOrUrl) {
        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
            return URI.create(pathOrUrl);
        }
        return URI.create(siteBase(site) + (pathOrUrl.startsWith("/") ? pathOrUrl : "/" + pathOrUrl));
    }

    private String siteBase(Site site) {
        return baseOverride
                .map(u -> stripTrailingSlash(u.toString()) + "/_/" + site.subdomain())
                .orElseGet(() -> "https://" + site.subdomain() + ".craigslist.org");
    }

    private String rootBase() {
        return baseOverride
                .map(u -> stripTrailingSlash(u.toString()))
                .orElse("https://www.craigslist.org");
    }

    private static String stripTrailingSlash(String s) {
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }

    private static String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
