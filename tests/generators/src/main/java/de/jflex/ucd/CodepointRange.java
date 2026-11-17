/*
 * Copyright (C) 2019-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import java.util.Comparator;

public final class CodepointRange {

  public static final Comparator<CodepointRange> COMPARATOR =
      Comparator.comparingInt(CodepointRange::start).thenComparingInt(CodepointRange::end);

  private final int start;
  private final int end;

  private CodepointRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  /** Start code-point, included. */
  public int start() {
    return start;
  }

  /** End code-point, included. */
  public int end() {
    return end;
  }

  public int length() {
    return end() - start();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof CodepointRange)) {
      return false;
    }
    CodepointRange other = (CodepointRange) o;
    return start == other.start && end == other.end;
  }

  @Override
  public int hashCode() {
    return 31 * start + end;
  }

  @Override
  public String toString() {
    if (length() == 0) {
      return String.format("\\u%04x", start());
    }
    return String.format("\\u%04x…\\u%04x", start(), end());
  }

  public static CodepointRange create(int start, int end) {
    return new CodepointRange(start, end);
  }

  public static CodepointRange createPoint(int codePoint) {
    return create(codePoint, codePoint);
  }

  public boolean contains(CodepointRange range) {
    return this.start() <= range.start() && range.end() <= this.end();
  }
}
