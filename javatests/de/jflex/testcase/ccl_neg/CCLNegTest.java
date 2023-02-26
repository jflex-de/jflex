/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_neg;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test character class syntax. */
public class CCLNegTest {

  @Test
  public void test() throws Exception {
    ScannerFactory<CCLNeg> scannerFactory = ScannerFactory.of(CCLNeg::new);
    CCLNeg scanner = scannerFactory.createScannerWithContent("not_space then\n space");

    assertThat(scanner.yylex()).isEqualTo(1); // not space
    assertThat(scanner.yylex()).isEqualTo(0); // default
    assertThat(scanner.yylex()).isEqualTo(1); // not space
    assertThat(scanner.yylex()).isEqualTo(2); // nl
    assertThat(scanner.yylex()).isEqualTo(0); // default
    assertThat(scanner.yylex()).isEqualTo(1); // not space
    assertThat(scanner.yylex()).isEqualTo(-1); // EOF
  }
}
