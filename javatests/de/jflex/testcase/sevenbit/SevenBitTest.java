/*
 * Copyright (C) 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.sevenbit;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test generation of a 7-bit scanner */
public class SevenBitTest {
  private final ScannerFactory<SevenBit> scannerFactory = ScannerFactory.of(SevenBit::new);

  @Test
  public void run() throws java.io.IOException {
    SevenBit scanner = scannerFactory.createScannerWithContent("abc\n\n");
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(-1);
  }
}
