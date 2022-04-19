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

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;

public class UnicodeDigitFlexGenerator extends AbstractGenerator<UnicodeDigitFlexTemplateVars> {

  private final String symbol;

  protected UnicodeDigitFlexGenerator(UnicodeVersion unicodeVersion, String symbol) {
    super("UnicodeDigit.flex", unicodeVersion);
    this.symbol = symbol;
  }

  @Override
  protected UnicodeDigitFlexTemplateVars createTemplateVars() {
    UnicodeDigitFlexTemplateVars vars = new UnicodeDigitFlexTemplateVars();
    vars.value = true;
    // Filesystem safe name
    String testName =
        symbol
            .replace('[', '-')
            .replace(']', '-')
            .replace(':', '-')
            .replace('\\', '-')
            .replaceAll("-", "");
    // Work around Bazel confusion of lower/upper case targets
    if (testName.length() == 1) {
      char c = testName.charAt(0);
      if (Character.isLowerCase(c)) {
        testName = "lower" + Character.toUpperCase(c);
      } else {
        // \D means not digit
        vars.value = false;
        testName = "upper" + c;
      }
    }
    vars.className = "UnicodeDigit_" + testName + "_" + unicodeVersion.underscoreVersion();
    vars.symbol = symbol;
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeDigitFlexTemplateVars vars) {
    return vars.className + ".flex";
  }
}
