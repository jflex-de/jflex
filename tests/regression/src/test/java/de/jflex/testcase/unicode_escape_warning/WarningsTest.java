/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode_escape_warning;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/**
 * Test warnings for and matching for unicode espace sequences that are too long.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/pull/183">#183</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/unicode_escape_warning/warnings.flex",
    sysout = "src/test/java/de/jflex/testcase/unicode_escape_warning/sys-out.txt",
    quiet = true)
public class WarningsTest implements JFlexTest {
  private final ScannerFactory<Warnings> scannerFactory = ScannerFactory.of(Warnings::new);

  @Test
  public void run() throws java.io.IOException {
    Warnings scanner = scannerFactory.createScannerWithContent("013245020210ab121201ab0");
    assertEquals(0, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(3, scanner.yylex());
    assertEquals(2, scanner.yylex());
    assertEquals(4, scanner.yylex());
    assertEquals(5, scanner.yylex());
    assertEquals(6, scanner.yylex());
    assertEquals(7, scanner.yylex());
    assertEquals(8, scanner.yylex());
    assertEquals(9, scanner.yylex());
    assertEquals(10, scanner.yylex());
    assertEquals(11, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(-1, scanner.yylex());
  }
}
