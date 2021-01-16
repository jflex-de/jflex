/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
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

package de.jflex.migration.unicodedatatest.testdigit;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.SurrogateUtils;

@AutoValue
abstract class DigitBlocks {

  abstract ImmutableList<BlockSpec<Boolean>> blocks();

  public static DigitBlocks.Builder builder() {
    return new AutoValue_DigitBlocks.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract ImmutableList.Builder<BlockSpec<Boolean>> blocksBuilder();

    abstract DigitBlocks build();

    public void add(boolean value, int start, int end) {
      CodepointRange range = CodepointRange.create(start, end);
      if (SurrogateUtils.containsSurrogate(range)) {
        blocksBuilder()
            .add(BlockSpec.create(value, start, SurrogateUtils.SURROGATE_RANGE.start() - 1));
        blocksBuilder().add(BlockSpec.create(value, SurrogateUtils.SURROGATE_RANGE.end() + 1, end));
      } else {
        blocksBuilder().add(BlockSpec.create(value, range));
      }
    }

    public void add(boolean value, CodepointRange codepointRange) {
      blocksBuilder().add(BlockSpec.create(value, codepointRange));
    }
  }
}