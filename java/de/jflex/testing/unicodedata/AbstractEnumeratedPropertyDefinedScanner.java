/*
 * Copyright (C) 2009-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import java.io.IOException;
import java.lang.reflect.Array;

public abstract class AbstractEnumeratedPropertyDefinedScanner<T> {
  private final int maxCodePoint;
  private final T[] propertyValues;

  protected AbstractEnumeratedPropertyDefinedScanner(int maxCodePoint, Class<T> clazz) {
    this.maxCodePoint = maxCodePoint;
    propertyValues = (T[]) Array.newInstance(clazz, maxCodePoint + 1);
  }

  public ImmutableList<NamedCodepointRange<T>> blocks() {
    ImmutableList.Builder<NamedCodepointRange<T>> blocks = ImmutableList.builder();
    T prevPropertyValue = propertyValues[0];
    int begCodePoint = 0;
    for (int codePoint = 1; codePoint <= maxCodePoint; ++codePoint) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        if (prevPropertyValue != null) {
          blocks.add(NamedCodepointRange.create(prevPropertyValue, begCodePoint, codePoint - 1));
        }
        begCodePoint = codePoint = 0xE000;
        prevPropertyValue = propertyValues[codePoint];
        continue;
      }
      T propertyValue = propertyValues[codePoint];
      if (null == propertyValue || !propertyValue.equals(prevPropertyValue)) {
        if (null != prevPropertyValue) {
          blocks.add(NamedCodepointRange.create(prevPropertyValue, begCodePoint, codePoint - 1));
        }
        prevPropertyValue = propertyValue;
        begCodePoint = codePoint;
      }
    }
    if (null != prevPropertyValue) {
      blocks.add(NamedCodepointRange.create(prevPropertyValue, begCodePoint, maxCodePoint));
    }
    return blocks.build();
  }

  public ImmutableList<CodepointRange> ranges() {
    return blocks().stream().map(NamedCodepointRange::range).collect(toImmutableList());
  }

  protected void setCurCharPropertyValue(String index, int length, T propertyValue) {
    int i = 0;
    while (i < length) {
      int codePoint = index.codePointAt(i);
      propertyValues[codePoint] = propertyValue;
      i += Character.charCount(codePoint);
    }
  }

  public T getPropertyValue(int codepoint) {
    return propertyValues[codepoint];
  }

  public abstract int yylex() throws IOException;
}
