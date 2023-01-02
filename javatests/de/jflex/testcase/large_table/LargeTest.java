/*
 * Copyright 2023, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_table;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test for <a href="https://github.com/jflex-de/jflex/issues/952">#952</a>. */
public class LargeTest {

  @Test
  public void largeTable() throws Exception {
    ScannerFactory<Large> scannerFactory = ScannerFactory.of(Large::new);
    // ((a|b)*b.{10}){3}
    Large scanner =
        scannerFactory.createScannerWithContent("xababbbbbbbbbbbabbbbbbbbbbbbababbbbbbbbbbbazaz");
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yytext()).isEqualTo("x");
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yytext()).isEqualTo("ababbbbbbbbbbbabbbbbbbbbbbbababbbbbbbbbbbazaz");
    assertThat(scanner.yylex()).isEqualTo(-1);
  }
}
