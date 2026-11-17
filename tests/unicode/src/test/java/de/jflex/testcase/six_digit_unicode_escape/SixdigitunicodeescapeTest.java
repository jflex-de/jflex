// test: SixDigitUnicodeEscape
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.six_digit_unicode_escape;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.util.scanner.ScannerFactory;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/** Tests {@code \UXXXXXX} six-digit hexadecimal character escape syntax. */
public class SixdigitunicodeescapeTest {

  /** Creates a scanner conforming to the {@code SixDigitUnicodeEscape.flex} specification. */
  private final ScannerFactory<SixDigitUnicodeEscape> scannerFactory =
      ScannerFactory.of(SixDigitUnicodeEscape::new);

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void unicodeParsed() throws Exception {

    SixDigitUnicodeEscape scanner = scannerFactory.createScannerWithContent(Dataset.ALL.content());
    scanner.yylex();
    assertEquals(
        Arrays.asList(
            "0000..0000; inverse matched",
            "0001..0001; matched",
            "0002..0002; inverse matched",
            "0003..D7FF; matched",
            "E000..10FFFD; matched",
            "10FFFE..10FFFF; inverse matched"),
        scanner.output());
  }
}
