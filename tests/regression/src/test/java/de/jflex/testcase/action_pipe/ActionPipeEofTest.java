/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.action_pipe;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test action piped with {@code <<EOF>>}. */
public class ActionPipeEofTest {
  private final ScannerFactory<ActionPipeEof> scannerFactory =
      ScannerFactory.of(ActionPipeEof::new);

  @Test
  public void tokenAtEOF() throws java.io.IOException {
    ActionPipeEof scanner = scannerFactory.createScannerWithContent("ident\n\ntest");
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
  }
}
