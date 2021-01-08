/*
 * Copyright (C) 2021 Google, LLC.
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

import com.google.auto.value.AutoValue;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.SurrogateUtils;

@AutoValue
public abstract class BlockSpec<T> {

  private static final String HEX_FORMAT = "0x%04X";

  public abstract T name();

  public abstract CodepointRange range();

  public static <T> BlockSpec<T> create(T name, int start, int end) {
    // TODO: trim
    return new AutoValue_BlockSpec<T>(name, CodepointRange.create(start, end));
  }

  public boolean isSurrogate() {
    return SurrogateUtils.containsSurrogate(range());
  }

  @Override
  public final String toString() {
    return String.format("%04X..%04X; %s", range().start(), range().end(), name());
  }

  public String hexStart() {
    return String.format(HEX_FORMAT, range().start());
  }

  public String hexEnd() {
    return String.format(HEX_FORMAT, range().end());
  }
}
