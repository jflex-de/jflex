/*
 * Copyright 2022, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_init;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/986">char class directives delete
 * existing classes #986</a>.
 */
public class CclInitTest {
  /** Creates a scanner conforming to the {@code ccl.flex} specification. */
  private final ScannerFactory<Ccl> scannerFactory = ScannerFactory.of(Ccl::new);

  @Test
  public void test_init() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("cab");
    assertEquals(1, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(-1, scanner.yylex());
  }
}
