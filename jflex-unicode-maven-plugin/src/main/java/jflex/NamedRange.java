package jflex;

import java.util.Objects;

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

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof NamedRange)) {
      return false;
    }
    NamedRange other = (NamedRange) obj;
    return start == other.start && end == other.end && Objects.equals(name, other.name);
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1_000_003;
    h ^= start;
    h *= 1_000_003;
    h ^= end;
    if (name != null) {
      h *= 1_000_003;
      h ^= name.hashCode();
    }
    return h;
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
