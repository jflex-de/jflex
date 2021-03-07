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
package de.jflex.testcase.unicode.unicode_3_2;

import static com.google.common.truth.Truth.assertWithMessage;

import de.jflex.testing.unicodedata.AbstractSimpleParser.PatternHandler;
import de.jflex.testing.unicodedata.SimpleGoldenCaselessParser;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.annotation.Generated;
import org.junit.Ignore;
import org.junit.Test;

/** Test Tests the {@code %caseless} directive for Unicode 3.2. */
@Generated("de.jflex.migration.unicodedatatest.testcaseless.UnicodeCaseselessTestGenerator")
public class UnicodeCaselessTest_3_2 {

  private final Path packageDirectory = Paths.get("javatests/de/jflex/testcase/unicode");

  @Test
  @Ignore // TODO(FIX) Character 0x10428 (=66600) should match caselessly 0x10400
  public void caseless() throws Exception {
    UnicodeCaseless_3_2 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeCaseless_3_2::new),
            UnicodeCaseless_3_2.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    PatternHandler expectationVerifier =
        new PatternHandler() {
          @Override
          public void onRegexMatch(List<String> regexpGroups) {
            String inputChar = regexpGroups.get(0);
            String expectedEquivalence = regexpGroups.get(1);
            int inputCodepoint = Integer.parseInt(inputChar, 16);
            int expected = Integer.parseInt(expectedEquivalence, 16);
            Integer actualEquivalence = scanner.getPropertyValue(inputCodepoint);
            assertWithMessage(
                    "Character 0x%s (=%s) matches caselessly 0x%s (=%s)",
                    inputChar, inputCodepoint, expectedEquivalence, expected)
                .that(actualEquivalence)
                .isEqualTo(expected);
          }
        };
    String goldenFile = "unicode_3_2/UnicodeCaseless_3_2.output";
    try (BufferedReader goldenReader =
        Files.newBufferedReader(packageDirectory.resolve(goldenFile))) {
      SimpleGoldenCaselessParser parser =
          new SimpleGoldenCaselessParser(goldenReader, expectationVerifier);
      parser.parse();
    }
  }
}
