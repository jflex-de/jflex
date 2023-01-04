// test: SixDigitUnicodeEscape
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
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
        .containsExactly(
            "0000..0000; inverse matched",
            "0001..0001; matched",
            "0002..0002; inverse matched",
            "0003..D7FF; matched",
            "E000..10FFFD; matched",
            "10FFFE..10FFFF; inverse matched")
        .inOrder();
  }
}
