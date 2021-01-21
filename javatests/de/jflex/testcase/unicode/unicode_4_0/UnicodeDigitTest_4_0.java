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
package de.jflex.testcase.unicode.unicode_4_0;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.AbstractSimpleParser.PatternHandler;
import de.jflex.testing.unicodedata.SimpleDigitParser;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.util.scanner.ScannerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.annotation.Generated;
import org.junit.BeforeClass;
import org.junit.Test;

/** Test Tests the {@code %caseless} directive for Unicode 4.0. */
@Generated("de.jflex.migration.unicodedatatest.testdigit.UnicodeDigitTestGenerator")
public class UnicodeDigitTest_4_0 {

  private static final Path packageDirectory = Paths.get("javatests/de/jflex/testcase/unicode");

  private static ImmutableList<NamedCodepointRange<Boolean>> expected;

  @BeforeClass
  public static void golden() throws Exception {
    expected = readGolden();
  }

  /**
   * Tests character class syntax of the Unicode 4_0 DecimalDigit property ({@code Nd}) using the
   * {@code [:digit:]} syntax.
   */
  @Test
  public void digit() throws Exception {
    UnicodeDigit_digit_4_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeDigit_digit_4_0::new),
            UnicodeDigit_digit_4_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.blocks()).containsExactlyElementsIn(expected);
  }

  /**
   * Tests character class syntax of the Unicode 4_0 DecimalDigit property ({@code Nd}) using the
   * {@code \D} syntax.
   */
  @Test
  public void upperD() throws Exception {
    UnicodeDigit_upperD_4_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeDigit_upperD_4_0::new),
            UnicodeDigit_upperD_4_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.blocks()).containsExactlyElementsIn(expected);
  }

  /**
   * Tests character class syntax of the Unicode 4_0 DecimalDigit property ({@code Nd}) using the
   * {@code \d} syntax.
   */
  @Test
  public void lowerD() throws Exception {
    UnicodeDigit_lowerD_4_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeDigit_lowerD_4_0::new),
            UnicodeDigit_lowerD_4_0.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertThat(scanner.blocks()).containsExactlyElementsIn(expected);
  }

  private static ImmutableList<NamedCodepointRange<Boolean>> readGolden() throws IOException {
    ImmutableList.Builder<NamedCodepointRange<Boolean>> expected = ImmutableList.builder();
    PatternHandler goldenHandler =
        new PatternHandler() {
          @Override
          public void onRegexMatch(List<String> regexpGroups) {
            int start = Integer.parseInt(regexpGroups.get(0), 16);
            int end = Integer.parseInt(regexpGroups.get(1), 16);
            boolean digit = regexpGroups.get(2).equals("Nd");
            expected.add(NamedCodepointRange.<Boolean>create(digit, start, end));
          }
        };
    String goldenFile = "unicode_4_0/UnicodeDigit_4_0.output";
    try (BufferedReader goldenReader =
        Files.newBufferedReader(packageDirectory.resolve(goldenFile))) {
      SimpleDigitParser parser = new SimpleDigitParser(goldenReader, goldenHandler);
      parser.parse();
    }
    return expected.build();
  }
}
