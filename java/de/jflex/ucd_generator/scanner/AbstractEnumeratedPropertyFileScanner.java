/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.NamedCodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.SortedSet;
import java.util.TreeSet;

/** Scans the common single-property Unicode.org data file format. */
abstract class AbstractEnumeratedPropertyFileScanner {

  final UnicodeData unicodeData;

  final SortedSet<NamedCodepointRange> intervals =
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
    for (NamedCodepointRange interval : intervals) {
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
