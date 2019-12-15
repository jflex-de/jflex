package jflex.ucd_generator.ucd;

import java.util.Objects;

/** Mutable version of the {@link CodepointRange}. */
public class MutableCodepointRange {
  public int start;
  public int end;

  public MutableCodepointRange(int startCodePoint, int endCodePoint) {
    start = startCodePoint;
    end = endCodePoint;
  }

  @Override
  public String toString() {
    if (start == end) {
      return String.valueOf(start);
    }
    return start + "…" + end;
  }

  public static MutableCodepointRange create(CodepointRange range) {
    return new MutableCodepointRange(range.start(), range.end());
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MutableCodepointRange)) {
      return false;
    }
    MutableCodepointRange other = (MutableCodepointRange) obj;
    return other.start == start && other.end == end;
  }
}
