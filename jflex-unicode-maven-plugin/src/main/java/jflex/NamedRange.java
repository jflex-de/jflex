package jflex;

/** Internal-use class to represent code point intervals. */
public class NamedRange implements Comparable<NamedRange> {
  int start;
  int end;
  String name;

  NamedRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  NamedRange(int start, int end, String name) {
    this.start = start;
    this.end = end;
    this.name = name;
  }

  public boolean equals(NamedRange other) {
    return null != other
        && (start == other.start
            && end == other.end
            && ((null == name && null == other.name) || (null != name && name.equals(other.name))));
  }

  public int compareTo(NamedRange other) {
    int comparison = 0;
    if (null != other) {
      comparison = Integer.valueOf(start).compareTo(other.start);
      if (0 == comparison) {
        comparison = Integer.valueOf(end).compareTo(other.end);
      }
    }
    return comparison;
  }
}
