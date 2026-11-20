/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode_escape_warning;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.IOException;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * Test warnings for and matching for unicode espace sequences that are too long.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/pull/183">#183</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/unicode_escape_warning/warnings.flex",
    warnings = {
      @Warn(value = ErrorMessages.UNICODE_TOO_LONG, line = 28, column = 7),
      @Warn(value = ErrorMessages.UNICODE_TOO_LONG, line = 29, column = 7),
      @Warn(value = ErrorMessages.UNICODE_TOO_LONG, line = 30, column = 9),
      @Warn(value = ErrorMessages.UNICODE_TOO_LONG, line = 32, column = 8),
      @Warn(value = ErrorMessages.UNICODE_TOO_LONG, line = 33, column = 8),
      @Warn(value = ErrorMessages.UNICODE_TOO_LONG, line = 34, column = 10)
    },
    sysout = "src/test/java/de/jflex/testcase/unicode_escape_warning/sys-out.txt",
    quiet = true)
public class WarningsTest implements JFlexTest {

  private Warnings scanner;

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  @Test
  public void run() throws java.io.IOException {
    scanner = new Warnings(new StringReader("013245020210ab121201ab0"));
    assertToken(0, "0");
    assertToken(1, "1");
    assertToken(3, "3");
    assertToken(2, "2");
    assertToken(4, "4");
    assertToken(5, "5");
    // the escapes that are too long: the extra characters are matched as themselves
    assertToken(6, "02");
    assertToken(7, "021");
    assertToken(8, "0ab");
    assertToken(9, "12");
    assertToken(10, "120");
    assertToken(11, "1ab");
    assertToken(0, "0");
    assertEquals(-1, scanner.yylex());
  }
}
