/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testdigit;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.SurrogateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class DigitBlocks {

  private final List<NamedCodepointRange<Boolean>> blocks;

  private DigitBlocks(List<NamedCodepointRange<Boolean>> blocks) {
    this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));
  }

  List<NamedCodepointRange<Boolean>> blocks() {
    return blocks;
  }

  public static DigitBlocks.Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private final List<NamedCodepointRange<Boolean>> blocks = new ArrayList<>();

    private Builder() {}

    DigitBlocks build() {
      return new DigitBlocks(blocks);
    }

    public void add(boolean value, int start, int end) {
      CodepointRange range = CodepointRange.create(start, end);
      if (SurrogateUtils.containsSurrogate(range)) {
        blocks.add(
            NamedCodepointRange.create(value, start, SurrogateUtils.SURROGATE_RANGE.start() - 1));
        blocks.add(
            NamedCodepointRange.create(value, SurrogateUtils.SURROGATE_RANGE.end() + 1, end));
      } else {
        blocks.add(NamedCodepointRange.create(value, range));
      }
    }

    public void add(boolean value, CodepointRange codepointRange) {
      blocks.add(NamedCodepointRange.create(value, codepointRange));
    }
  }
}
