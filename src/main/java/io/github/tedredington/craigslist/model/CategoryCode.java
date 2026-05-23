package io.github.tedredington.craigslist.model;

/** Common supertype for the per-kind category enums ({@link HousingCategory},
 *  {@link ForSaleCategory}, {@link JobsCategory}, {@link GigsCategory},
 *  {@link EventsCategory}, {@link CommunityCategory}, {@link ResumesCategory},
 *  {@link ServicesCategory}). Each query subclass accepts only its own kind,
 *  so cross-category mixing is a compile error. */
public sealed interface CategoryCode
        permits HousingCategory,
                ForSaleCategory,
                JobsCategory,
                GigsCategory,
                EventsCategory,
                CommunityCategory,
                ResumesCategory,
                ServicesCategory {

    /** The URL path segment Craigslist expects (e.g. {@code "apa"} for apartments). */
    String code();

    /** Human-readable name. */
    String displayName();
}
