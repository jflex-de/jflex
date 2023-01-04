/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.resources;

public abstract class AbstractUnicodeAllBinaryProperty {

  private static final int maxCodePoint = 0x10FFFF;
  private final boolean[] propertyValues = new boolean[maxCodePoint + 1];

  private void printOutput() {
    int codePoint = 0;
    while (codePoint <= maxCodePoint) {
      // First, find start point
      while (codePoint <= maxCodePoint && !propertyValues[codePoint]) {
        ++codePoint;
      }
      if (codePoint > maxCodePoint) {
        break; // No start point available
      }
      int begin = codePoint;
      // Second, find next undefined point (one char beyond the end point)
      while (codePoint <= maxCodePoint && propertyValues[codePoint]) {
        ++codePoint;
      }
      int end = codePoint - 1;
      System.out.format("%04X..%04X%n", begin, end);
    }
  }

  private void setCurCharPropertyValue(String value) {
    propertyValues[value.codePointAt(0)] = true;
  }
}
