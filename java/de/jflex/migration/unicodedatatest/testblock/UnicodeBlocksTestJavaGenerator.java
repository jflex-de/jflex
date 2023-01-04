/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.Versions;
import java.util.Comparator;

class UnicodeBlocksTestJavaGenerator
    extends AbstractBlocksGenerator<UnicodeBlocksTestJavaTemplateVars, String> {

  // The first (high) surrogate is a 16-bit code value in the range U+D800 to U+DBFF. The second
  // (low) surrogate is a 16-bit code value in the range U+DC00 to U+DFFF.
  private static final CodepointRange SURROGATES = CodepointRange.create(0xD800, 0xDFFF);
  private static final String NO_BLOCK = "No Block";

  public UnicodeBlocksTestJavaGenerator(
      UnicodeVersion unicodeVersion, ImmutableList<NamedCodepointRange<String>> blockNames) {
    super("UnicodeBlocksTest.java", unicodeVersion, blockNames);
  }

  @Override
  protected UnicodeBlocksTestJavaTemplateVars createTemplateVars() {
    UnicodeBlocksTestJavaTemplateVars vars = new UnicodeBlocksTestJavaTemplateVars();
    vars.className = "UnicodeBlocksTest_" + unicodeVersion.underscoreVersion();
    Comparator<NamedCodepointRange> comparator =
        (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());
    vars.blocks = ImmutableSortedSet.copyOf(comparator, addNoBlock(blocks));
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeBlocksTestJavaTemplateVars vars) {
    return vars.className + ".java";
  }

  private ImmutableList<NamedCodepointRange<String>> addNoBlock(
      ImmutableList<NamedCodepointRange<String>> blocks) {
    ImmutableList.Builder<NamedCodepointRange<String>> retval = ImmutableList.builder();
    retval.add(blocks.get(0));
    for (int i = 1; i < blocks.size(); i++) {
      // end of the prev block
      int prevEnd = blocks.get(i - 1).range().end();
      // start of the block
      int nextStart = blocks.get(i).range().start();
      if (prevEnd + 1 != nextStart) {
        NamedCodepointRange<String> noBlock =
            NamedCodepointRange.create(NO_BLOCK, prevEnd + 1, nextStart - 1);
        if (noBlock.range().contains(SURROGATES)) {
          noBlock = NamedCodepointRange.create(NO_BLOCK, prevEnd + 1, SURROGATES.start() - 1);
        }
        if (!SURROGATES.contains(noBlock.range())) {
          retval.add(noBlock);
        }
      }
      retval.add(blocks.get(i));
    }
    NamedCodepointRange<String> lastBlock = blocks.get(blocks.size() - 1);
    int maxCodePoint = Versions.maxCodePoint(unicodeVersion.version());
    if (lastBlock.range().end() != maxCodePoint) {
      retval.add(NamedCodepointRange.create(NO_BLOCK, lastBlock.range().end() + 1, maxCodePoint));
    }
    return retval.build();
  }
}
