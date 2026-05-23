package io.github.tedredington.craigslist.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

/** A single Craigslist listing. The fields populated depend on whether the listing
 *  came from a search-results page (basic fields) or from {@code fetchDetail}
 *  (which additionally fills {@link #body()} and {@link #geotag()} when available).
 *  Category-specific extras (e.g. bedrooms for housing) live in {@link #customFields()}. */
public record Listing(
        String id,
        String title,
        URI url,
        Optional<OffsetDateTime> datetimePosted,
        OptionalInt priceCents,
        Optional<String> location,
        boolean hasImage,
        Optional<Geotag> geotag,
        Map<String, String> customFields,
        Optional<String> body) {

    public Listing {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must be non-blank");
        }
        if (title == null) {
            throw new IllegalArgumentException("title must be non-null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        datetimePosted = datetimePosted == null ? Optional.empty() : datetimePosted;
        priceCents = priceCents == null ? OptionalInt.empty() : priceCents;
        location = location == null ? Optional.empty() : location;
        geotag = geotag == null ? Optional.empty() : geotag;
        body = body == null ? Optional.empty() : body;
        customFields =
                (customFields == null) ? Map.of() : Map.copyOf(customFields);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(id)
                .title(title)
                .url(url)
                .datetimePosted(datetimePosted.orElse(null))
                .priceCents(priceCents.isPresent() ? Integer.valueOf(priceCents.getAsInt()) : null)
                .location(location.orElse(null))
                .hasImage(hasImage)
                .geotag(geotag.orElse(null))
                .customFields(customFields)
                .body(body.orElse(null));
    }

    /** Mutable builder for {@link Listing}. Used by parsers and {@code fetchDetail}. */
    public static final class Builder {
        private String id;
        private String title;
        private URI url;
        private OffsetDateTime datetimePosted;
        private Integer priceCents;
        private String location;
        private boolean hasImage;
        private Geotag geotag;
        private Map<String, String> customFields = new LinkedHashMap<>();
        private String body;

        private Builder() {}

        public Builder id(String v) { this.id = v; return this; }
        public Builder title(String v) { this.title = v; return this; }
        public Builder url(URI v) { this.url = v; return this; }
        public Builder datetimePosted(OffsetDateTime v) { this.datetimePosted = v; return this; }
        public Builder priceCents(Integer v) { this.priceCents = v; return this; }
        public Builder location(String v) { this.location = v; return this; }
        public Builder hasImage(boolean v) { this.hasImage = v; return this; }
        public Builder geotag(Geotag v) { this.geotag = v; return this; }
        public Builder body(String v) { this.body = v; return this; }

        public Builder customFields(Map<String, String> v) {
            this.customFields = (v == null) ? new LinkedHashMap<>() : new LinkedHashMap<>(v);
            return this;
        }

        public Builder putCustom(String key, String value) {
            this.customFields.put(key, value);
            return this;
        }

        public Listing build() {
            return new Listing(
                    id,
                    title,
                    url,
                    Optional.ofNullable(datetimePosted),
                    priceCents == null ? OptionalInt.empty() : OptionalInt.of(priceCents),
                    Optional.ofNullable(location),
                    hasImage,
                    Optional.ofNullable(geotag),
                    customFields,
                    Optional.ofNullable(body));
        }
    }
}
