/*
 * Copyright (C) 2019-2020 Google, LLC.
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
package de.jflex.ucd_generator.ucd;

import de.jflex.ucd.CodepointRange;
import java.util.Comparator;
import java.util.Objects;

/** Mutable version of the {@link CodepointRange}. */
public class MutableCodepointRange {

  static final Comparator<MutableCodepointRange> COMPARATOR_START_POINT =
      Comparator.comparingInt(o -> o.start);

  public int start;
  public int end;

  private MutableCodepointRange(int startCodePoint, int endCodePoint) {
    start = startCodePoint;
    end = endCodePoint;
  }

  @Override
  public String toString() {
    if (start == end) {
      return String.format("\\u%04x", start);
    }
    return String.format("\\u%04x" + "…" + "\\u%04x", start, end);
  }

  public static MutableCodepointRange create(CodepointRange range) {
    return MutableCodepointRange.create(range.start(), range.end());
  }

  public static MutableCodepointRange create(int startCodePoint, int endCodePoint) {
    return new MutableCodepointRange(startCodePoint, endCodePoint);
  }

  public static MutableCodepointRange createPoint(int codePoint) {
    return MutableCodepointRange.create(codePoint, codePoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MutableCodepointRange)) {
      return false;
    }
    MutableCodepointRange other = (MutableCodepointRange) obj;
    return other.start == start && other.end == end;
  }

  public CodepointRange toImmutableRange() {
    return CodepointRange.create(start, end);
  }
}
