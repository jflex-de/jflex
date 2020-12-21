package de.jflex.ucd_generator.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.jflex.ucd_generator.ucd.CodepointRange;
import java.util.regex.Pattern;

public class SurrogateUtils {

  private static final Pattern SURROGATE_PATTERN =
      Pattern.compile("^cs$|surrogate", Pattern.CASE_INSENSITIVE);

  /** Returns whether the property represent a surrogate [U+D800-U+DFFF]. */
  public static boolean isSurrogate(String propName) {
    return SURROGATE_PATTERN.matcher(propName).find();
  }

  /**
   * Returns 0, 1, or 2 ranges for the given interval, depending on whether it is contained within;
   * is entirely outside of or starts or ends within; or straddles the surrogate range
   * [0xD800-0xDFFF], respectively.
   */
  public static ImmutableList<CodepointRange> removeSurrogates(
      int startCodePoint, int endCodePoint) {
    Preconditions.checkArgument(startCodePoint <= endCodePoint);
    if (startCodePoint >= 0xD800 && endCodePoint <= 0xDFFF) {
      return ImmutableList.of();
    }
    if (endCodePoint < 0xD800 || startCodePoint > 0xDFFF) {
      return ImmutableList.of(CodepointRange.create(startCodePoint, endCodePoint));
    }
    ImmutableList.Builder<CodepointRange> ranges = ImmutableList.builder();
    if (startCodePoint < 0xD800) {
      ranges.add(CodepointRange.create(startCodePoint, 0xD7FF));
    }
    if (endCodePoint > 0xDFFF) {
      ranges.add(CodepointRange.create(0xE000, endCodePoint));
    }
    return ranges.build();
  }

  private SurrogateUtils() {}
}
