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

import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/** Scans the common multiple binary property Unicode.org data file format. */
abstract class AbstractBinaryPropertiesFileScanner {

  private final UnicodeData unicodeData;

  String propertyName;
  int start;
  int end;

  private final HashMap<String, SortedSet<CodepointRange>> properties = new HashMap<>();

  public AbstractBinaryPropertiesFileScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  public void addPropertyIntervals() {
    for (Map.Entry<String, SortedSet<CodepointRange>> property : properties.entrySet()) {
      String currentPropertyName = property.getKey();
      SortedSet<CodepointRange> intervals = property.getValue();
      int prevEnd = -1;
      int prevStart = -1;
      for (CodepointRange interval : intervals) {
        if (prevEnd == -1) {
          prevStart = interval.start();
        } else if (interval.start() > prevEnd + 1) {
          unicodeData.addBinaryPropertyInterval(currentPropertyName, prevStart, prevEnd);
          prevStart = interval.start();
        }
        prevEnd = interval.end();
      }
      // Add final interval
      unicodeData.addBinaryPropertyInterval(currentPropertyName, prevStart, prevEnd);
    }
    properties.clear();
  }

  public void addCurrentInterval() {
    SortedSet<CodepointRange> intervals =
        properties.computeIfAbsent(propertyName, k -> new TreeSet<>(CodepointRange.COMPARATOR));
    intervals.add(CodepointRange.create(start, end));
  }
}
