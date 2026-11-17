// test: ccl-esc
/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_esc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/**
 * bug-test for [".."] style character escapes (<a
 * href="https://github.com/jflex-de/jflex/issues/48">#48</a>)
 */
public class CclTest {
  /** Creates a scanner conforming to the {@code ccl.flex} specification. */
  private final ScannerFactory<Ccl> scannerFactory = ScannerFactory.of(Ccl::new);

  @Test
  public void tokenOther_a() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("a");
    assertEquals(Token.OTHER, scanner.yylex());
  }

  @Test
  public void tokenOther_hello() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("hello");
    assertEquals(Token.OTHER, scanner.yylex());
  }

  @Test
  public void tokenAB() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("ab");
    assertEquals(Token.AB, scanner.yylex());
  }
}
