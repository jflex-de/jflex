/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testblock;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

class UnicodeBlocksTestJavaGenerator
    extends AbstractBlocksGenerator<UnicodeBlocksTestJavaTemplateVars, String> {

  // The first (high) surrogate is a 16-bit code value in the range U+D800 to U+DBFF. The second
  // (low) surrogate is a 16-bit code value in the range U+DC00 to U+DFFF.
  private static final CodepointRange SURROGATES = CodepointRange.create(0xD800, 0xDFFF);
  private static final String NO_BLOCK = "No Block";

  public UnicodeBlocksTestJavaGenerator(
      UnicodeVersion unicodeVersion, List<NamedCodepointRange<String>> blockNames) {
    super("UnicodeBlocksTest.java", unicodeVersion, blockNames);
  }

  @Override
  protected UnicodeBlocksTestJavaTemplateVars createTemplateVars() {
    UnicodeBlocksTestJavaTemplateVars vars = new UnicodeBlocksTestJavaTemplateVars();
    vars.className = "UnicodeBlocksTest_" + unicodeVersion.underscoreVersion();
    Comparator<NamedCodepointRange> comparator =
        (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());
    StringBuilder blockRanges = new StringBuilder();
    SortedSet<NamedCodepointRange> sortedBlocks = new TreeSet<>(comparator);
    sortedBlocks.addAll(addNoBlock(blocks));
    for (NamedCodepointRange b : sortedBlocks) {
      if (blockRanges.length() > 0) {
        blockRanges.append(",\n");
      }
      blockRanges.append(
          String.format(
              "        NamedCodepointRange.create(\"%s\", 0x%s, 0x%s)",
              b.name(), b.hexStart(), b.hexEnd()));
    }
    vars.blockRanges = blockRanges.toString();
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeBlocksTestJavaTemplateVars vars) {
    return vars.className + ".java";
  }

  private List<NamedCodepointRange<String>> addNoBlock(List<NamedCodepointRange<String>> blocks) {
    List<NamedCodepointRange<String>> retval = new ArrayList<>();
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
    return Collections.unmodifiableList(retval);
  }
}
