/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.action_pipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

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
    assertEquals(1, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(0, scanner.yylex());
  }
}
