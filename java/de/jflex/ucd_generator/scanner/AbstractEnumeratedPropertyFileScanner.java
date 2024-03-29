/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.SortedSet;
import java.util.TreeSet;

/** Scans the common single-property Unicode.org data file format. */
abstract class AbstractEnumeratedPropertyFileScanner {

  final UnicodeData unicodeData;

  final SortedSet<NamedCodepointRange<String>> intervals =
      new TreeSet<>(NamedCodepointRange.START_COMPARATOR);

  String propertyName;
  String defaultPropertyValue;
  int start;
  int end;

  protected AbstractEnumeratedPropertyFileScanner(
      UnicodeData unicodeData, String defaultPropertyName, String defaultPropertyValue) {
    this.unicodeData = unicodeData;
    this.propertyName = defaultPropertyName;
    this.defaultPropertyValue = defaultPropertyValue;
  }

  public void addInterval(int start, int end, String name) {
    intervals.add(NamedCodepointRange.create(name, start, end));
  }

  public void addPropertyValueIntervals() {
    int prevEnd = -1;
    int prevStart = -1;
    String prevValue = "";
    for (NamedCodepointRange<String> interval : intervals) {
      if (interval.start() > prevEnd + 1) {
        // Unassigned code points get the default property value, e.g. "Unknown"
        unicodeData.addEnumPropertyInterval(
            propertyName, defaultPropertyValue, prevEnd + 1, interval.start() - 1);
      }
      if (prevEnd == -1) {
        prevStart = interval.start();
        prevValue = interval.name();
      } else if (interval.start() > prevEnd + 1 || !interval.name().equals(prevValue)) {
        unicodeData.addEnumPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
        prevStart = interval.start();
        prevValue = interval.name();
      }
      prevEnd = interval.end();
    }

    // Add final default property value interval, if necessary
    if (prevEnd < unicodeData.maximumCodePoint()) {
      unicodeData.addEnumPropertyInterval(
          propertyName, defaultPropertyValue, prevEnd + 1, unicodeData.maximumCodePoint());
    }

    // Add final named interval
    unicodeData.addEnumPropertyInterval(propertyName, prevValue, prevStart, prevEnd);
  }
}
