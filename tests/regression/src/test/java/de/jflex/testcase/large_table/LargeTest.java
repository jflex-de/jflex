/*
 * Copyright 2023, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_table;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/** Test for <a href="https://github.com/jflex-de/jflex/issues/952">#952</a>. */
public class LargeTest {

  @Test
  public void largeTable() throws Exception {
    ScannerFactory<Large> scannerFactory = ScannerFactory.of(Large::new);
    // ((a|b)*b.{10}){3}
    Large scanner =
        scannerFactory.createScannerWithContent("xababbbbbbbbbbbabbbbbbbbbbbbababbbbbbbbbbbazaz");
    assertEquals(0, scanner.yylex());
    assertEquals("x", scanner.yytext());
    assertEquals(1, scanner.yylex());
    assertEquals("ababbbbbbbbbbbabbbbbbbbbbbbababbbbbbbbbbbazaz", scanner.yytext());
    assertEquals(-1, scanner.yylex());
  }
}
