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
package de.jflex.migration.unicodedatatest.testblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.Versions;
import java.util.Comparator;

class UnicodeBlocksTestJavaGenerator
    extends AbstractBlocksGenerator<UnicodeBlocksTestJavaTemplateVars, String> {

  // The first (high) surrogate is a 16-bit code value in the range U+D800 to U+DBFF. The second
  // (low) surrogate is a 16-bit code value in the range U+DC00 to U+DFFF.
  private static final CodepointRange SURROGATES = CodepointRange.create(0xD800, 0xDFFF);
  private static final String NO_BLOCK = "No Block";

  public UnicodeBlocksTestJavaGenerator(
      UnicodeVersion unicodeVersion, ImmutableList<BlockSpec<String>> blockNames) {
    super("UnicodeBlocksTest.java", unicodeVersion, blockNames);
  }

  @Override
  protected UnicodeBlocksTestJavaTemplateVars createTemplateVars() {
    UnicodeBlocksTestJavaTemplateVars vars = new UnicodeBlocksTestJavaTemplateVars();
    vars.className = "UnicodeBlocksTest_" + unicodeVersion.underscoreVersion();
    Comparator<BlockSpec> comparator =
        (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());
    vars.blocks = ImmutableSortedSet.copyOf(comparator, addNoBlock(blocks));
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeBlocksTestJavaTemplateVars vars) {
    return vars.className + ".java";
  }

  private ImmutableList<BlockSpec<String>> addNoBlock(ImmutableList<BlockSpec<String>> blocks) {
    ImmutableList.Builder<BlockSpec<String>> retval = ImmutableList.builder();
    retval.add(blocks.get(0));
    for (int i = 1; i < blocks.size(); i++) {
      // end of the prev block
      int prevEnd = blocks.get(i - 1).range().end();
      // start of the block
      int nextStart = blocks.get(i).range().start();
      if (prevEnd + 1 != nextStart) {
        BlockSpec<String> noBlock = BlockSpec.create(NO_BLOCK, prevEnd + 1, nextStart - 1);
        if (noBlock.range().contains(SURROGATES)) {
          noBlock = BlockSpec.create(NO_BLOCK, prevEnd + 1, SURROGATES.start() - 1);
        }
        if (!SURROGATES.contains(noBlock.range())) {
          retval.add(noBlock);
        }
      }
      retval.add(blocks.get(i));
    }
    BlockSpec<String> lastBlock = blocks.get(blocks.size() - 1);
    int maxCodePoint = Versions.maxCodePoint(unicodeVersion.version());
    if (lastBlock.range().end() != maxCodePoint) {
      retval.add(BlockSpec.create(NO_BLOCK, lastBlock.range().end() + 1, maxCodePoint));
    }
    return retval.build();
  }
}
