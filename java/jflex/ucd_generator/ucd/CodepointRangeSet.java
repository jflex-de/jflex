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

    public Builder substract(CodepointRange substractingRange) {
      if (mRanges.isEmpty()) {
        return this;
      }
      SortedSet<MutableCodepointRange> lastRanges =
          substractIntervalsStartingAfter(substractingRange);
      // Also consider the last interval which was starting before
      try {
        MutableCodepointRange lastRange = lastRanges.last();
        if (lastRange.start < substractingRange.start()) {
          if (lastRange.end < substractingRange.end()) {
            lastRange.end = substractingRange.start() - 1;
          } else {
            mRanges.add(MutableCodepointRange.create(substractingRange.end() + 1, lastRange.end));
            lastRange.end = substractingRange.start() - 1;
          }
        }
      } catch (NoSuchElementException e) {
        // lastRange remains null
      }

      return this;
    }

    /**
     * Remove intervals starting after substractingRange.start
     *
     * @return Ranges that may intersect before subsrtactingRange.end
     */
    private SortedSet<MutableCodepointRange> substractIntervalsStartingAfter(
        CodepointRange substractingRange) {
      MutableCodepointRange start = MutableCodepointRange.create(substractingRange.start());
      MutableCodepointRange end = MutableCodepointRange.create(substractingRange.end());
      // Remove ranges after the start, up to end
      SortedSet<MutableCodepointRange> potentiallyRemoved = mRanges.subSet(start, end);
      List<MutableCodepointRange> toRemove = new ArrayList<>();
      for (MutableCodepointRange r : potentiallyRemoved) {
        if (r.end <= substractingRange.end()) {
          toRemove.add(r);
        } else {
          r.start = substractingRange.end() + 1;
        }
      }

      SortedSet<MutableCodepointRange> ranges =
          potentiallyRemoved.isEmpty() ? mRanges : mRanges.headSet(potentiallyRemoved.first());

      potentiallyRemoved.removeAll(toRemove);
      return ranges;
    }

    abstract CodepointRangeSet internalBuild();

    public CodepointRangeSet build() {
      rangesBuilder().addAll(mRanges.stream().map(CodepointRange::create).iterator());
      return internalBuild();
    }
  }
}
