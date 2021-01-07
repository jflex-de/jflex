/*
 * Copyright (C) 2009-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
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
package de.jflex.testing.unicodedata;

import com.google.common.collect.ImmutableList;
import java.io.IOException;

public abstract class AbstractEnumeratedPropertyDefinedScanner {
  private final int maxCodePoint;
  private final String[] propertyValues;

  protected AbstractEnumeratedPropertyDefinedScanner(int maxCodePoint) {
    this.maxCodePoint = maxCodePoint;
    propertyValues = new String[maxCodePoint + 1];
  }

  public ImmutableList<BlockSpec> blocks() {
    ImmutableList.Builder<BlockSpec> blocks = ImmutableList.builder();
    String prevPropertyValue = propertyValues[0];
    int begCodePoint = 0;
    for (int codePoint = 1; codePoint <= maxCodePoint; ++codePoint) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        if (null != prevPropertyValue) {
          blocks.add(BlockSpec.create(prevPropertyValue, begCodePoint, codePoint - 1));
        }
        begCodePoint = codePoint = 0xE000;
        prevPropertyValue = propertyValues[codePoint];
        continue;
      }
      String propertyValue = propertyValues[codePoint];
      if (null == propertyValue || !propertyValue.equals(prevPropertyValue)) {
        if (null != prevPropertyValue) {
          blocks.add(BlockSpec.create(prevPropertyValue, begCodePoint, codePoint - 1));
        }
        prevPropertyValue = propertyValue;
        begCodePoint = codePoint;
      }
    }
    if (null != prevPropertyValue) {
      blocks.add(BlockSpec.create(prevPropertyValue, begCodePoint, maxCodePoint));
    }
    return blocks.build();
  }

  protected void setCurCharPropertyValue(String index, String propertyValue) {
    propertyValues[index.codePointAt(0)] = propertyValue;
  }

  public String getPropertyValue(int codepoint) {
    return propertyValues[codepoint];
  }

  public abstract int yylex() throws IOException;
}
