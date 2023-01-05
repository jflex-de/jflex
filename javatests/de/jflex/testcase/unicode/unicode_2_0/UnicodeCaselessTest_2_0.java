/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode.unicode_2_0;

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
import org.junit.Test;

/** Test Tests the {@code %caseless} directive for Unicode 2.0. */
@Generated("de.jflex.migration.unicodedatatest.testcaseless.UnicodeCaseselessTestGenerator")
public class UnicodeCaselessTest_2_0 {

  private final Path packageDirectory = Paths.get("javatests/de/jflex/testcase/unicode");

  @Test
  public void caseless() throws Exception {
    UnicodeCaseless_2_0 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeCaseless_2_0::new),
            UnicodeCaseless_2_0.YYEOF,
            UnicodeDataScanners.Dataset.BMP);
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
    String goldenFile = "unicode_2_0/UnicodeCaseless_2_0.output";
    try (BufferedReader goldenReader =
        Files.newBufferedReader(packageDirectory.resolve(goldenFile))) {
      SimpleGoldenCaselessParser parser =
          new SimpleGoldenCaselessParser(goldenReader, expectationVerifier);
      parser.parse();
    }
  }
}
