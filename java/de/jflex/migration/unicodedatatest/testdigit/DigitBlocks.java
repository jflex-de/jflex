/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.SurrogateUtils;

@AutoValue
abstract class DigitBlocks {

  abstract ImmutableList<NamedCodepointRange<Boolean>> blocks();

  public static DigitBlocks.Builder builder() {
    return new AutoValue_DigitBlocks.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract ImmutableList.Builder<NamedCodepointRange<Boolean>> blocksBuilder();

    abstract DigitBlocks build();

    public void add(boolean value, int start, int end) {
      CodepointRange range = CodepointRange.create(start, end);
      if (SurrogateUtils.containsSurrogate(range)) {
        blocksBuilder()
            .add(
                NamedCodepointRange.create(
                    value, start, SurrogateUtils.SURROGATE_RANGE.start() - 1));
        blocksBuilder()
            .add(NamedCodepointRange.create(value, SurrogateUtils.SURROGATE_RANGE.end() + 1, end));
      } else {
        blocksBuilder().add(NamedCodepointRange.create(value, range));
      }
    }

    public void add(boolean value, CodepointRange codepointRange) {
      blocksBuilder().add(NamedCodepointRange.create(value, codepointRange));
    }
  }
}
