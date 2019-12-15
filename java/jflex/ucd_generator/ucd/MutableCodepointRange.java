package jflex.ucd_generator.ucd;

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
}
