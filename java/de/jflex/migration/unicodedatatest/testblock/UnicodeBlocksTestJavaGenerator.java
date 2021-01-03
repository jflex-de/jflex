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

package de.jflex.migration.unicodedatatest.testblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.ucd.CodepointRange;
import java.nio.file.Path;
import java.util.Comparator;

public class UnicodeBlocksTestJavaGenerator
    extends AbstractBlocksGenerator<UnicodeBlocksTestJavaTemplateVars> {

  public UnicodeBlocksTestJavaGenerator(
      UnicodeVersion unicodeVersion, ImmutableList<BlockSpec> blockNames) {
    super("UnicodeBlocksTest.java.vm", "UnicodeBlocksTest", unicodeVersion, blockNames);
  }

  @Override
  protected UnicodeBlocksTestJavaTemplateVars createTemplateVars() {
    UnicodeBlocksTestJavaTemplateVars vars = new UnicodeBlocksTestJavaTemplateVars();
    vars.className = "UnicodeBlocksTest_" + unicodeVersion.underscoreVersion();
    Comparator<BlockSpec> comparator =
        (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());
    vars.blocks = ImmutableSortedSet.copyOf(comparator, this.blocks);
    return vars;
  }

  @Override
  protected Path getOuputFilePath(Path outDir, UnicodeBlocksTestJavaTemplateVars vars) {
    return outDir.resolve(vars.className + ".java");
  }
}