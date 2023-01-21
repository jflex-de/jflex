/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode_escape_warning;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test warnings for and matching for unicode espace sequences that are too long.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/pull/183">#183</a>.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/unicode_escape_warning/warnings.flex",
    sysout = "javatests/de/jflex/testcase/unicode_escape_warning/sys-out.txt",
    quiet = true)
public class WarningsTest {
  private final ScannerFactory<Warnings> scannerFactory = ScannerFactory.of(Warnings::new);

  @Test
  public void run() throws java.io.IOException {
    Warnings scanner = scannerFactory.createScannerWithContent("013245020210ab121201ab0");
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(3);
    assertThat(scanner.yylex()).isEqualTo(2);
    assertThat(scanner.yylex()).isEqualTo(4);
    assertThat(scanner.yylex()).isEqualTo(5);
    assertThat(scanner.yylex()).isEqualTo(6);
    assertThat(scanner.yylex()).isEqualTo(7);
    assertThat(scanner.yylex()).isEqualTo(8);
    assertThat(scanner.yylex()).isEqualTo(9);
    assertThat(scanner.yylex()).isEqualTo(10);
    assertThat(scanner.yylex()).isEqualTo(11);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(-1);
  }
}
