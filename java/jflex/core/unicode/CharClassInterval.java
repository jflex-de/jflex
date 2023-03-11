/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

/**
 * Stores an interval of characters together with the character class
 *
 * <p>A character belongs to an interval, if its Unicode value is greater than or equal to the
 * Unicode value of {@code start} and smaller than or equal to the Unicode value of <CODE>end
 * </code>.
 *
 * <p>All characters of the interval must belong to the same character class.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class CharClassInterval {

  /** The first character of the interval */
  public final int start;

  /** The last character of the interval */
  public final int end;

  /** The code of the class all characters of this interval belong to. */
  public final int charClass;

  /**
   * Creates a new CharClassInterval from {@code start</code> to <CODE>end} that belongs to
   * character class {@code charClass}.
   *
   * @param start The first character of the interval
   * @param end The last character of the interval
   * @param charClass The code of the class all characters of this interval belong to.
   */
  public CharClassInterval(int start, int end, int charClass) {
    this.start = start;
    this.end = end;
    this.charClass = charClass;
  }

  /** Determines wether the specified code point is in this interval. */
  public boolean contains(int codePoint) {
    return start <= codePoint && codePoint <= end;
  }

  @Override
  public String toString() {
    return "[" + start + "-" + end + "=" + charClass + "]";
  }
}
