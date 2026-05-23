# Test fixtures

HTML snapshots of Craigslist pages used by unit tests via `MockWebServer`.
These are intentionally hand-trimmed to keep the repo small while still exercising
the parsers' real selectors.

## Refresh procedure

When Craigslist's DOM changes and a parser falls back / breaks, capture a fresh
snapshot:

```bash
curl -A "java-craigslist-fixture-refresh" \
    "https://sfbay.craigslist.org/search/sfc/apa" \
    > housing/page-0.html
```

Then trim aggressively — delete `<script>` blocks, ads, footer chrome, etc.
Keep only enough DOM for the parser to do its work. Review the diff; commit.

## Files

- `housing/page-0.html` — three rows with prices, time, hood, housing blurb.
- `housing/page-empty.html` — zero rows + `cl-no-results` marker.
