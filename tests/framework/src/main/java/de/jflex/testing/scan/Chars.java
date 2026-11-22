/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.scan;

/** Text for characters in the failure messages of tests. */
public final class Chars {

  private Chars() {}

  /** Returns the code point in the form {@code U+0041}. */
  public static String hex(int codePoint) {
    return String.format("U+%04X", codePoint);
  }

  /** Returns every {@code char} of the text as a Unicode escape, {@code A\u000A}. */
  public static String hex(String text) {
    StringBuilder result = new StringBuilder();
    for (char c : text.toCharArray()) {
      appendEscape(result, c);
    }
    return result.toString();
  }

  /**
   * Returns the text in quotes, with a Unicode escape for every {@code char} that is not printable
   * ASCII, and cut off after {@code maxLength} chars of the text.
   */
  static String quoted(String text, int maxLength) {
    StringBuilder result = new StringBuilder("\"");
    for (int i = 0; i < text.length() && i < maxLength; i++) {
      char c = text.charAt(i);
      if (c >= ' ' && c <= '~' && c != '"' && c != '\\') {
        result.append(c);
      } else {
        appendEscape(result, c);
      }
    }
    result.append('"');
    if (text.length() > maxLength) {
      result.append("... (").append(text.length()).append(" chars)");
    }
    return result.toString();
  }

  private static void appendEscape(StringBuilder result, char c) {
    result.append(String.format("\\u%04X", (int) c));
  }
}
