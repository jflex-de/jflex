package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import java.util.Comparator;

@AutoValue
public abstract class CodepointRange {

  /** Start code-point, included. */
  abstract int start();

  /** End code-point, included. */
  abstract int end();

  public static CodepointRange create(int start, int end) {
    return new AutoValue_CodepointRange(start, end);
  }

  public final int length() {
    return end() - start();
  }

  @Override
  public final String toString() {
    if (length() == 0) {
      return "[" + start() + "]";
    }
    return "[" + start() + "…" + end() + "]";
  }

  public static Builder builder(int startCodePoint, int endCodePoint) {
    return new Builder(startCodePoint, endCodePoint);
  }

  /** Mutable version of the CodePointRange. */
  public static class Builder {
    public int start;
    public int end;

    public Builder(int startCodePoint, int endCodePoint) {
      start = startCodePoint;
      end = endCodePoint;
    }

    public CodepointRange build() {
      return create(start, end);
    }
  }

  public static final Comparator<CodepointRange> START_COMPARATOR =
      (left, right) -> Integer.compare(left.start(), right.start());
}
