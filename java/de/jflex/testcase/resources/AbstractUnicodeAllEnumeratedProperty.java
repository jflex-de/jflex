/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
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
