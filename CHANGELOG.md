# Changelog

All notable changes to this project are documented here. Format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial library skeleton: Gradle build, Maven Central publishing config.
- Type-safe `Craigslist` client with eight category subclasses
  (`CraigslistCommunity`, `CraigslistEvents`, `CraigslistForSale`,
  `CraigslistGigs`, `CraigslistHousing`, `CraigslistJobs`, `CraigslistResumes`,
  `CraigslistServices`), each implementing `Iterable<Listing>` with transparent
  auto-pagination.
- `Site` / `Area` / `Region` enums for compile-time-safe location selection.
- Per-kind `CategoryCode` enums (`HousingCategory`, `ForSaleCategory`, …).
- Robust exception hierarchy rooted at `CraigslistException`.
