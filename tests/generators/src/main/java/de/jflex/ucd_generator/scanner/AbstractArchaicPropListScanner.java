/*
 * Copyright (C) 2009 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AbstractArchaicPropListScanner {

  final UnicodeData unicodeData;

  /** Map of propName -> intervals */
  Map<String, Set<CodepointRange>> intervals = new HashMap<>();

  String propertyName;
  int start;
  int end;

  public AbstractArchaicPropListScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  public void addPropertyIntervals() {
    for (Map.Entry<String, Set<CodepointRange>> property : intervals.entrySet()) {
      String currentPropertyName = property.getKey();
      for (CodepointRange interval : property.getValue()) {
        unicodeData.addBinaryPropertyInterval(currentPropertyName, interval);
      }
    }
  }

  public void addCurrentInterval() {
    intervals
        .computeIfAbsent(propertyName, k -> new HashSet<>())
        .add(CodepointRange.create(start, end));
  }
}
