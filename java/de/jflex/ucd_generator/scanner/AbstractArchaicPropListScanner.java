/*
 * Copyright (C) 2009 Steve Rowe <sarowe@gmail.com>
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

import com.google.common.collect.HashMultimap;
import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.Map;

public class AbstractArchaicPropListScanner {

  final UnicodeData unicodeData;

  /** Map of propName -> intervals */
  HashMultimap<String, CodepointRange> intervals = HashMultimap.create();

  String propertyName;
  int start;
  int end;

  public AbstractArchaicPropListScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  public void addPropertyIntervals() {
    for (Map.Entry<String, CodepointRange> property : intervals.entries()) {
      String currentPropertyName = property.getKey();
      CodepointRange interval = property.getValue();
      unicodeData.addBinaryPropertyInterval(currentPropertyName, interval);
    }
  }

  public void addCurrentInterval() {
    intervals.put(propertyName, CodepointRange.create(start, end));
  }
}
