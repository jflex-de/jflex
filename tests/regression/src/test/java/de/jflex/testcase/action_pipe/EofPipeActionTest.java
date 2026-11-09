/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.action_pipe;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Regression test for issue #201.
 *
 * <p>{@code <<EOF>>} action doesn't compile when piped with another action.
 */
public class EofPipeActionTest {
  private final ScannerFactory<EofPipeAction> scannerFactory =
      ScannerFactory.of(EofPipeAction::new);

  @Test
  public void tokenAtEOF() throws java.io.IOException {
    EofPipeAction scanner = scannerFactory.createScannerWithContent("ident\n\ntest");
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
  }
}
