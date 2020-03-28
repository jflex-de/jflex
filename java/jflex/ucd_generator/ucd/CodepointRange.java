package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import java.util.Comparator;

@AutoValue
public abstract class CodepointRange {

  public static final Comparator<CodepointRange> START_COMPARATOR =
      (left, right) -> Integer.compare(left.start(), right.start());

  /** Start code-point, included. */
  public abstract int start();

  /** End code-point, included. */
  public abstract int end();

  public final int length() {
    return end() - start();
  }

  @Override
  public final String toString() {
    if (length() == 0) {
      return String.valueOf(start());
    }
    return start() + "…" + end();
  }

  public static CodepointRange create(int start, int end) {
    return new AutoValue_CodepointRange(start, end);
  }

  public static CodepointRange create(MutableCodepointRange mutableCodepointRange) {
    return create(mutableCodepointRange.start, mutableCodepointRange.end);
  }
}
