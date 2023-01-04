/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.resources;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUnicodeAllEnumeratedProperty {

  private static final int maxCodePoint = 0x10FFFF;
  private final String[] propertyValues = new String[maxCodePoint + 1];
  private final List<String> output = new ArrayList<>();

  public List<String> output() {
    return output;
  }

  public void eof() {
    String prevPropertyValue = propertyValues[0];
    int begCodePoint = 0;
    for (int codePoint = 1; codePoint <= maxCodePoint; ++codePoint) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        output.add(formatBock(begCodePoint, codePoint - 1, prevPropertyValue));
        begCodePoint = codePoint = 0xE000;
        prevPropertyValue = propertyValues[codePoint];
        continue;
      }
      String propertyValue = propertyValues[codePoint];
      if (null == propertyValue || !propertyValue.equals(prevPropertyValue)) {
        output.add(formatBock(begCodePoint, codePoint - 1, prevPropertyValue));
        prevPropertyValue = propertyValue;
        begCodePoint = codePoint;
      }
    }
    output.add(formatBock(begCodePoint, maxCodePoint, prevPropertyValue));
  }

  private static String formatBock(int begCodePoint, int endCodePoint, String propertyValue) {
    return format("%04X..%04X; %s", begCodePoint, endCodePoint, propertyValue);
  }

  protected void setCurCharPropertyValue(String propertyValue, int length, String text) {
    int index = 0;
    while (index < length) {
      int codePoint = text.codePointAt(index);
      propertyValues[codePoint] = propertyValue;
      index += Character.charCount(codePoint);
    }
  }
}
