/*
 * Copyright (C) 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.sevenbit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/** Test generation of a 7-bit scanner */
public class SevenBitTest {
  private final ScannerFactory<SevenBit> scannerFactory = ScannerFactory.of(SevenBit::new);

  @Test
  public void run() throws java.io.IOException {
    SevenBit scanner = scannerFactory.createScannerWithContent("abc\n\n");
    assertEquals(1, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(-1, scanner.yylex());
  }
}
