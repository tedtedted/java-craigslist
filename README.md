# java-craigslist

A type-safe Java 21 client for searching [Craigslist](https://craigslist.org)
listings. Inspired by [python-craigslist](https://github.com/juliomalegria/python-craigslist).

## Install

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("io.github.tedtedted:java-craigslist:0.1.0")
}
```

Maven:

```xml
<dependency>
    <groupId>io.github.tedtedted</groupId>
    <artifactId>java-craigslist</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Quick start

```java
import io.github.tedtedted.craigslist.*;
import io.github.tedtedted.craigslist.model.*;

Craigslist cl = Craigslist.create();

CraigslistHousing housing = CraigslistHousing.builder(cl)
    .site(Site.SF_BAY).area(Area.SFC)
    .category(HousingCategory.APARTMENTS)
    .minPrice(1500).maxPrice(3000)
    .hasImage(true)
    .build();

for (Listing l : housing) {
    System.out.println(l);
    // Listing[id=…, title=Sunny 1BR, url=…, priceCents=240000, location=Mission, hasImage=true]
    // (empty Optionals/maps are automatically omitted)
}
```

The query *is* the result: each `CraigslistXxx` implements `Iterable<Listing>`
with transparent auto-pagination (capped at Craigslist's 3000-listing limit).

## More usage

```java
// Optional<Listing>
Optional<Listing> first = housing.first();

// Stream<Listing>
List<Listing> top20 = housing.stream().limit(20).toList();

// Approximate total result count, from the first page's totalcount
long approx = housing.approximateCount();

// Fetch the full detail page (body + geotag) for a listing
Listing detailed = housing.fetchDetail(first.orElseThrow());

// Configure timeouts, retries, user-agent, rate limit
try (Craigslist tuned = Craigslist.builder()
        .userAgent("MyBot/1.0 (+https://example.com)")
        .requestTimeout(Duration.ofSeconds(30))
        .maxRetries(5)
        .minDelayBetweenRequests(Duration.ofMillis(500))
        .build()) {
    CraigslistJobs jobs = CraigslistJobs.builder(tuned)
        .site(Site.NEW_YORK)
        .employmentType(EmploymentType.FULL_TIME)
        .query("java")
        .build();
    jobs.forEach(System.out::println);
}
```

## Type safety

Each category's `Builder` exposes only the filters that apply to it. Mixing
filters across categories is a compile error, not a runtime surprise:

```java
CraigslistGigs.builder(cl)
    .site(Site.SF_BAY)
    .pets(Pets.DOGS_OK)   // ❌ won't compile — pets isn't a Gigs filter
    .build();
```

`Site` and `Area` are enums; an `Area` whose parent site doesn't match the
selected `Site` raises `InvalidFilterException` at `.build()` time:

```java
CraigslistHousing.builder(cl)
    .site(Site.SF_BAY)
    .area(Area.MANHATTAN)   // ❌ InvalidFilterException at build()
    .build();
```

## Error handling

All exceptions are unchecked and rooted at `CraigslistException`. Common
patterns:

```java
try {
    for (Listing l : housing) handle(l);
} catch (CraigslistRateLimitException e) {
    Thread.sleep(e.retryAfter().orElse(Duration.ofMinutes(1)).toMillis());
} catch (CraigslistBlockedException e) {
    log.error("Blocked at {} — rotate UA / slow down", e.uri());
} catch (CraigslistTransportException e) {
    // any network/HTTP failure
} catch (CraigslistException e) {
    // parsing or config error
}
```

Note: an **empty result is not an exception**. `housing.first()` returns
`Optional.empty()`, `housing.approximateCount()` returns 0, and iteration
produces zero elements.

## Categories

| Class | Default category | Notable filters |
|---|---|---|
| `CraigslistCommunity` | `ccc` | (base only) |
| `CraigslistEvents` | `eee` | `category` |
| `CraigslistForSale` | `sss` | `minPrice`, `maxPrice`, `condition`, `make` |
| `CraigslistGigs` | `ggg` | `isPaid` |
| `CraigslistHousing` | `hhh` | `minPrice`, `maxPrice`, bedrooms, bathrooms, sqft, `pets`, `laundry`, `parking`, … |
| `CraigslistJobs` | `jjj` | `employmentType`, `internship`, `telecommute` |
| `CraigslistResumes` | `rrr` | `employmentType` |
| `CraigslistServices` | `bbb` | (base only) |

## Requirements

- Java 21+

## License

MIT — see [LICENSE](LICENSE).
