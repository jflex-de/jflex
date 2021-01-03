// test: SixDigitUnicodeEscape
/*
 * Copyright (C) 2019-2020 Google, LLC.
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
package de.jflex.testcase.six_digit_unicode_escape;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/** Tests {@code \UXXXXXX} six-digit hexadecimal character escape syntax. */
public class SixdigitunicodeescapeTest {

  /** Creates a scanner conforming to the {@code SixDigitUnicodeEscape.flex} specification. */
  private final ScannerFactory<SixDigitUnicodeEscape> scannerFactory =
      ScannerFactory.of(SixDigitUnicodeEscape::new);

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void unicodeParsed() throws Exception {

    File inputFile =
        new File("testsuite/testcases/src/test/resources/All.Unicode.characters.input");
    SixDigitUnicodeEscape scanner = scannerFactory.createScannerForFile(inputFile);
    scanner.yylex();
    assertThat(scanner.output())
        // TODO(regisd) Replace the string comparison by value object.
        .containsAllOf(
            "0000..0000; inverse matched",
            "0001..0001; matched",
            "0002..0002; inverse matched",
            "0003..D7FF; matched",
            "E000..10FFFD; matched",
            "10FFFE..10FFFF; inverse matched")
        .inOrder();
  }
}
