package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@AutoValue
public abstract class CodepointRangeSet {

  public abstract ImmutableList<CodepointRange> ranges();

  public static Builder builder() {
    return new AutoValue_CodepointRangeSet.Builder();
  }

  public List<MutableCodepointRange> toMutableList() {
    return ranges().stream().map(MutableCodepointRange::create).collect(Collectors.toList());
  }

  @AutoValue.Builder
  public abstract static class Builder {
    private final TreeSet<MutableCodepointRange> mRanges =
        new TreeSet<>(MutableCodepointRange.COMPARATOR_START_POINT);

    abstract ImmutableList.Builder<CodepointRange> rangesBuilder();

    public Builder addAllImmutable(Collection<CodepointRange> ranges) {
      if (ranges == null) {
        return this;
      }
      for (CodepointRange range : ranges) {
        add(range);
      }
      return this;
    }

    public Builder addAll(Collection<MutableCodepointRange> ranges) {
      if (ranges == null) {
        return this;
      }
      for (MutableCodepointRange range : ranges) {
        add(range);
      }
      return this;
    }

    public Builder add(CodepointRange range) {
      return add(MutableCodepointRange.create(range));
    }

    // This assumes ranges are added in order
    public Builder add(MutableCodepointRange range) {
      if (!mRanges.isEmpty()) {
        MutableCodepointRange last = mRanges.last();
        if (last.end + 1 == range.start) {
          last.end = range.end;
          return this;
        }
      }
      mRanges.add(range);
      return this;
    }

    /**
     * Removes all values in the {@link CodepointRangeSet} that are withing {@code substractingRange}.
     *
     * <p>This method assumes intervals are ordered and non-overlapping.
     */
    public Builder substract(CodepointRange substractingRange) {
      if (mRanges.isEmpty()) {
        return this;
      }
      List<MutableCodepointRange> intersection =
          intersection(substractingRange);
      if (intersection.isEmpty()) {
        return this;
      }
      MutableCodepointRange first = intersection.get(0);
      if (first.end > substractingRange.start()) {
        // Maybe the first is partially intersecting
        if (first.end > substractingRange.end()) {
          mRanges.add(MutableCodepointRange.create(substractingRange.end() + 1, first.end));
        }
        if (first.start < substractingRange.start()) {
          first.end = substractingRange.start() - 1;
          intersection.remove(0);
        }
      }

      if (!intersection.isEmpty()) {
        // Maybe the last is only partially intersecting
        MutableCodepointRange last = intersection.get(intersection.size() - 1);
        if (last.start < substractingRange.end() && substractingRange.end() < last.end) {
          last.start =  substractingRange.end() + 1;
          intersection.remove(intersection.size() - 1);
        }
        mRanges.removeAll(intersection);
      }

      return this;
    }

    /**
     * Returns all intervals that intersect with intersecting.
     */
    private List<MutableCodepointRange> intersection(
        CodepointRange intersecting) {
      List<MutableCodepointRange> intersection = new ArrayList<>();

      MutableCodepointRange start = MutableCodepointRange.create(intersecting.start());
      MutableCodepointRange end = MutableCodepointRange.create(intersecting.end());

      SortedSet<MutableCodepointRange> subset = mRanges.subSet(start, end);

      // Also consider the last interval which was starting before
      try {
        SortedSet<MutableCodepointRange> prevRanges =
            subset.isEmpty() ? mRanges : mRanges.headSet(subset.first());
        MutableCodepointRange prevRange = prevRanges.last();
        if (prevRange.start < intersecting.start()) {
          intersection.add(0, prevRange);
        }
      } catch (NoSuchElementException e) {
        // lastRange remains null
      }

      intersection.addAll(subset);

      return intersection;
    }

    abstract CodepointRangeSet internalBuild();

    public CodepointRangeSet build() {
      rangesBuilder().addAll(mRanges.stream().map(CodepointRange::create).iterator());
      return internalBuild();
    }
  }
}
