package de.jflex.ucd_generator.ucd;

import java.util.Comparator;
import java.util.Objects;

/** Mutable version of the {@link CodepointRange}. */
public class MutableCodepointRange {

  static final Comparator<MutableCodepointRange> COMPARATOR_START_POINT =
      Comparator.comparingInt(o -> o.start);

  public int start;
  public int end;

  private MutableCodepointRange(int startCodePoint, int endCodePoint) {
    start = startCodePoint;
    end = endCodePoint;
  }

  @Override
  public String toString() {
    if (start == end) {
      return String.format("\\u%04x", start);
    }
    return String.format("\\u%04x" + "…" + "\\u%04x", start, end);
  }

  public static MutableCodepointRange create(CodepointRange range) {
    return MutableCodepointRange.create(range.start(), range.end());
  }

  public static MutableCodepointRange create(int startCodePoint, int endCodePoint) {
    return new MutableCodepointRange(startCodePoint, endCodePoint);
  }

  public static MutableCodepointRange createPoint(int codePoint) {
    return MutableCodepointRange.create(codePoint, codePoint);
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
