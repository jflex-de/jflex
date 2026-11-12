/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

/** The text that the Unicode tests scan: every code point in order, without the surrogates. */
public final class AllCodePoints {

  /** Returns the code points from 0 to {@code last} in order, without surrogates. */
  public static String upTo(int last) {
    StringBuilder text = new StringBuilder(last + 1 + Math.max(0, last - 0xFFFF));
    for (int codePoint = 0; codePoint <= last; codePoint++) {
      if (codePoint < Character.MIN_SURROGATE || codePoint > Character.MAX_SURROGATE) {
        text.appendCodePoint(codePoint);
      }
    }
    return text.toString();
  }

  private AllCodePoints() {}
}
