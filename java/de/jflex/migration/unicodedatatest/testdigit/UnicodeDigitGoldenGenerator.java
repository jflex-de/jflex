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

package de.jflex.migration.unicodedatatest.testdigit;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.ucd_generator.ucd.UnicodeData;

public class UnicodeDigitGoldenGenerator extends AbstractGenerator<UnicodeDigitGoldenTemplateVars> {

  private static final String TEMPLATE_NAME = "UnicodeDigitGolden";

  private final UnicodeData unicodeData;

  protected UnicodeDigitGoldenGenerator(UnicodeVersion unicodeVersion, UnicodeData unicodeData) {
    super(TEMPLATE_NAME, unicodeVersion);
    this.unicodeData = unicodeData;
  }

  @Override
  protected UnicodeDigitGoldenTemplateVars createTemplateVars() {
    UnicodeDigitGoldenTemplateVars vars = new UnicodeDigitGoldenTemplateVars();
    vars.className = "UnicodeDigit_" + unicodeVersion.underscoreVersion();
    vars.digitBlocks = createDigitBlocks(Versions.maxCodePoint(unicodeVersion.version()));
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeDigitGoldenTemplateVars vars) {
    return vars.className + ".output";
  }

  private ImmutableList<BlockSpec<Boolean>> createDigitBlocks(int maxCodepoint) {
    ImmutableList<CodepointRange> ranges = unicodeData.getPropertyValueIntervals("Nd");
    DigitBlocks.Builder digitBlocks = DigitBlocks.builder();
    digitBlocks.add(false, 0, ranges.get(0).start() - 1);
    digitBlocks.add(true, ranges.get(0));
    for (int i = 1; i < ranges.size(); i++) {
      digitBlocks.add(false, ranges.get(i - 1).end() + 1, ranges.get(i).start() - 1);
      digitBlocks.add(true, ranges.get(i));
    }
    digitBlocks.add(false, ranges.get(ranges.size() - 1).end() + 1, maxCodepoint);
    return digitBlocks.build().blocks();
  }
}
