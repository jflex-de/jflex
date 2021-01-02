/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2019-2020 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

@AutoValue
public abstract class CodepointRangeSet {

  public abstract ImmutableList<CodepointRange> ranges();

  public static Builder builder() {
    return new AutoValue_CodepointRangeSet.Builder();
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

    public void add(NamedCodepointRange interval) {
      add(interval.range());
    }

    public Builder add(CodepointRange range) {
      return add(MutableCodepointRange.create(range));
    }

    public Builder add(MutableCodepointRange range) {
      mRanges.add(range);
      return this;
    }

    public Builder substractAll(Collection<CodepointRange> substractingRanges) {
      int end = mRanges.last().end;
      for (CodepointRange substractingRange : substractingRanges) {
        if (substractingRange.start() > end) {
          return this;
        }
        substract(substractingRange);
      }
      return this;
    }

    /**
     * Removes all values in the {@link CodepointRangeSet} that are within {@code
     * substractingRange}.
     *
     * <p>This method assumes intervals are ordered and non-overlapping.
     */
    public Builder substract(CodepointRange substractingRange) {
      if (mRanges.isEmpty()) {
        return this;
      }
      List<MutableCodepointRange> intersection = intersection(substractingRange);
      if (intersection.isEmpty()) {
        return this;
      }
      MutableCodepointRange first = intersection.get(0);
      sub(first, substractingRange);
      MutableCodepointRange last = intersection.get(intersection.size() - 1);
      if (!last.equals(first)) {
        sub(last, substractingRange);
      }
      if (intersection.size() > 2) {
        mRanges.removeAll(intersection.subList(1, intersection.size() - 1));
      }
      return this;
    }

    /** Removes sub from range and updates mRanges. */
    private void sub(MutableCodepointRange range, CodepointRange sub) {
      if (sub.start() <= range.start && sub.end() >= range.end) {
        mRanges.remove(range);
      } else if (range.start < sub.start() && sub.end() < range.end) {
        // sub is a strict subset
        mRanges.add(MutableCodepointRange.create(sub.end() + 1, range.end));
        range.end = sub.start() - 1;
      } else if (sub.start() <= range.start) {
        range.start = sub.end() + 1;
      } else if (range.end <= sub.end()) {
        range.end = sub.start() - 1;
      } else {
        throw new IllegalStateException(
            String.format("Cannot remove sub=%s from range=%s", sub, range));
      }
    }

    /** Returns all intervals that intersect with intersecting. */
    @VisibleForTesting
    List<MutableCodepointRange> intersection(CodepointRange intersecting) {
      List<MutableCodepointRange> intersection = new ArrayList<>();

      MutableCodepointRange start = MutableCodepointRange.createPoint(intersecting.start());
      MutableCodepointRange end = MutableCodepointRange.createPoint(intersecting.end() + 1);
      // All intervals that start strictly after the intersecting (but not after its end)
      SortedSet<MutableCodepointRange> subset = mRanges.subSet(start, end);

      // Also consider the last interval which was starting before
      try {
        SortedSet<MutableCodepointRange> prevRanges =
            subset.isEmpty() ? mRanges.headSet(end) : mRanges.headSet(subset.first());
        MutableCodepointRange prevRange = prevRanges.last();
        if (prevRange.start < intersecting.start() && intersecting.start() <= prevRange.end) {
          intersection.add(0, prevRange);
        }
      } catch (NoSuchElementException e) {
        // lastRange remains null
      }

      intersection.addAll(subset);

      return intersection;
    }

    public CodepointRangeSet build() {
      Preconditions.checkState(!mRanges.isEmpty(), "Cannot create an empty set");
      internalAddRanges();
      return internalBuild();
    }

    private void internalAddRanges() {
      MutableCodepointRange lastRange = null;
      for (MutableCodepointRange r : mRanges) {
        if (lastRange == null) {
          lastRange = r;
          continue;
        }
        if (lastRange.end + 1 == r.start) {
          lastRange.end = r.end;
        } else {
          internalAddRange(lastRange);
          lastRange = r;
        }
      }
      internalAddRange(lastRange);
    }

    private void internalAddRange(MutableCodepointRange range) {
      CodepointRange immutableRange = range.toImmutableRange();
      rangesBuilder().add(immutableRange);
    }

    abstract CodepointRangeSet internalBuild();
  }
}
