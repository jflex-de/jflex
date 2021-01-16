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
package de.jflex.ucd;

import com.google.auto.value.AutoValue;
import java.util.Comparator;

@AutoValue
public abstract class NamedCodepointRange<T> {

  public static final Comparator<NamedCodepointRange> START_COMPARATOR =
      (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());

  private static final String HEX_FORMAT = "%04X";

  public abstract T name();

  public abstract CodepointRange range();

  public static <T> NamedCodepointRange<T> create(T name, CodepointRange range) {
    return new AutoValue_NamedCodepointRange(name, range);
  }

  public static <T> NamedCodepointRange<T> create(T name, int start, int end) {
    return create(name, CodepointRange.create(start, end));
  }

  @Override
  public final String toString() {
    return String.format("%04X..%04X; %s", range().start(), range().end(), name());
  }

  public int start() {
    return range().start();
  }

  public int end() {
    return range().end();
  }

  public String hexStart() {
    return String.format(HEX_FORMAT, range().start());
  }

  public String hexEnd() {
    return String.format(HEX_FORMAT, range().end());
  }

  public boolean isSurrogate() {
    return SurrogateUtils.containsSurrogate(range());
  }
}
