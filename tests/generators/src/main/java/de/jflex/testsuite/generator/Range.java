/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import java.util.Locale;

/** A range of code points, {@code start} to {@code end} inclusive, with a property value. */
final class Range {
  final int start;
  final int end;

  /** The property value, or null for the ranges of a binary property. */
  final String value;

  Range(int start, int end, String value) {
    if (start > end) {
      throw new IllegalArgumentException(
          String.format(Locale.ROOT, "Empty range %04X..%04X", start, end));
    }
    this.start = start;
    this.end = end;
    this.value = value;
  }

  Range(int start, int end) {
    this(start, end, null);
  }

  /** The same range with another value. */
  Range withValue(String newValue) {
    return new Range(start, end, newValue);
  }

  /**
   * The range as the goldens print it: {@code 0041..005A; Lu}, or {@code 0041..005A} without a
   * value.
   */
  String format() {
    String range = String.format(Locale.ROOT, "%04X..%04X", start, end);
    return value == null ? range : range + "; " + value;
  }

  @Override
  public String toString() {
    return format();
  }
}
