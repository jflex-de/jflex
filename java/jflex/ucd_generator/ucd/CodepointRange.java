package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Range;

@AutoValue
public abstract class CodepointRange {
  /** Start code-point, included. */
  abstract int start();
  /** End code-point, included. */
  abstract int end();

  public static CodepointRange create(int start, int end) {
    return new AutoValue_CodepointRange(start, end);
  }

  Range<Integer> toRange() {
    return Range.closed(start(), end());
  }
}
