package io.github.tedtedted.craigslist.pagination;

import io.github.tedtedted.craigslist.model.Listing;
import io.github.tedtedted.craigslist.model.SearchPage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * Drives lazy, sequential pagination across Craigslist search pages. Each call to {@link
 * #tryAdvance(Consumer)} drains a per-page buffer; when the buffer is empty it fetches the next
 * page until either the result cap is hit or an empty page is returned. Not splittable.
 */
public final class ListingSpliterator implements Spliterator<Listing> {

  /** Craigslist limits each search to the first 3000 results. */
  public static final int RESULT_CAP = 3000;

  private final IntFunction<SearchPage> fetcher;
  private final Deque<Listing> buffer = new ArrayDeque<>();
  private int nextOffset = 0;
  private boolean exhausted = false;
  private long known = Long.MAX_VALUE;

  public ListingSpliterator(IntFunction<SearchPage> fetcher) {
    this.fetcher = fetcher;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Listing> action) {
    while (buffer.isEmpty()) {
      if (exhausted || nextOffset >= RESULT_CAP) {
        return false;
      }
      SearchPage page = fetcher.apply(nextOffset);
      if (page.listings().isEmpty()) {
        exhausted = true;
        return false;
      }
      for (Listing l : page.listings()) {
        buffer.addLast(l);
      }
      int prev = nextOffset;
      nextOffset += page.listings().size();
      known = Math.min((long) page.totalCount(), (long) RESULT_CAP);
      if (!page.hasNext() || nextOffset <= prev) {
        exhausted = true;
      }
    }
    action.accept(buffer.removeFirst());
    return true;
  }

  @Override
  public Spliterator<Listing> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    if (known == Long.MAX_VALUE) {
      return Long.MAX_VALUE;
    }
    return Math.max(0L, known - nextOffset + buffer.size());
  }

  @Override
  public int characteristics() {
    return ORDERED | NONNULL | IMMUTABLE;
  }
}
