/*
 * Copyright (C) 2019-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import com.google.auto.value.AutoValue;
import java.util.Comparator;

@AutoValue
public abstract class CodepointRange {

  public static final Comparator<CodepointRange> COMPARATOR =
      Comparator.comparingInt(CodepointRange::start).thenComparingInt(CodepointRange::end);

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
      return String.format("\\u%04x", start());
    }
    return String.format("\\u%04x…\\u%04x", start(), end());
  }

  public static CodepointRange create(int start, int end) {
    return new AutoValue_CodepointRange(start, end);
  }

  public static CodepointRange createPoint(int codePoint) {
    return create(codePoint, codePoint);
  }

  public boolean contains(CodepointRange range) {
    return this.start() <= range.start() && range.end() <= this.end();
  }
}
