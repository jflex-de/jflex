/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2019-2021 Google, LLC.
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
package de.jflex.ucd;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.regex.Pattern;

public class SurrogateUtils {

  private static final Pattern SURROGATE_PATTERN =
      Pattern.compile("^cs$|surrogate", Pattern.CASE_INSENSITIVE);

  /** Start (incl) of the surrogate range. */
  private static final int START = 0xD800;
  /** End (incl) of the surrogate range. */
  public static final int END = 0xDFFF;

  public static final CodepointRange SURROGATE_RANGE = CodepointRange.create(START, END);

  /** Returns whether the property represent a surrogate [U+D800-U+DFFF]. */
  public static boolean isSurrogateProperty(String propName) {
    return SURROGATE_PATTERN.matcher(propName).find();
  }

  /**
   * Returns 0, 1, or 2 ranges for the given interval, depending on whether it is contained within;
   * is entirely outside of or starts or ends within; or straddles the surrogate range
   * [0xD800-0xDFFF], respectively.
   */
  public static ImmutableList<CodepointRange> removeSurrogates(
      int startCodePoint, int endCodePoint) {
    Preconditions.checkArgument(startCodePoint <= endCodePoint);
    if (startCodePoint >= START && endCodePoint <= END) {
      return ImmutableList.of();
    }
    if (endCodePoint < START || startCodePoint > END) {
      return ImmutableList.of(CodepointRange.create(startCodePoint, endCodePoint));
    }
    ImmutableList.Builder<CodepointRange> ranges = ImmutableList.builder();
    if (startCodePoint < START) {
      ranges.add(CodepointRange.create(startCodePoint, START - 1));
    }
    if (endCodePoint > END) {
      ranges.add(CodepointRange.create(END + 1, endCodePoint));
    }
    return ranges.build();
  }

  public static boolean containsSurrogate(CodepointRange cp) {
    return isSurrogate(cp.start())
        || isSurrogate(cp.end())
        || (cp.start() <= START && cp.end() >= END);
  }

  private static boolean isSurrogate(int cp) {
    return START <= cp && cp <= END;
  }

  private SurrogateUtils() {}
}
