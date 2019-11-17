package jflex.ucd_generator.ucd;

/** Mutable version of the {@link CodepointRange}. */
public class MutableCodepointRange {
  public int start;
  public int end;

  public MutableCodepointRange(int startCodePoint, int endCodePoint) {
    start = startCodePoint;
    end = endCodePoint;
  }
}
