package de.jflex.ucd_generator.ucd;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.regex.Pattern;

public class SurrogateUtils {

  private static final Pattern SURROGATE_PATTERN =
      Pattern.compile("^cs$|surrogate", Pattern.CASE_INSENSITIVE);

  /** Start (incl) of the surrogate range. */
  private static final int START = 0xD800;
  /** End (incl) of the surrogate range. */
  public static final int END = 0xDFFF;

  public static final CodepointRange SURROGATE_RANGE = CodepointRange.create(START, END);

  /** Returns whether the property represent a surrogate [U+D800-U+DFFF]. */
  public static boolean isSurrogateProperty(String propName) {
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
    if (startCodePoint >= START && endCodePoint <= END) {
      return ImmutableList.of();
    }
    if (endCodePoint < START || startCodePoint > END) {
      return ImmutableList.of(CodepointRange.create(startCodePoint, endCodePoint));
    }
    ImmutableList.Builder<CodepointRange> ranges = ImmutableList.builder();
    if (startCodePoint < START) {
      ranges.add(CodepointRange.create(startCodePoint, START - 1));
    }
    if (endCodePoint > END) {
      ranges.add(CodepointRange.create(END + 1, endCodePoint));
    }
    return ranges.build();
  }

  public static boolean containsSurrogate(CodepointRange cp) {
    return isSurrogate(cp.start())
        || isSurrogate(cp.end())
        || (cp.start() <= START && cp.end() >= END);
  }

  private static boolean isSurrogate(int cp) {
    return START <= cp && cp <= END;
  }

  private SurrogateUtils() {}
}
