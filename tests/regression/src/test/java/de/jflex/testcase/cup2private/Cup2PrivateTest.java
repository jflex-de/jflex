// test: cup2private
/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.cup2private;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/**
 * Regression test against <a href="https://github.com/jflex-de/jflex/issues/125">#125 %apiprivate
 * and %cup2 switches are incompatible </a>
 *
 * <p>The {@code %apiprivate} switch should not override the public visibility spec of {@link
 * edu.tum.cup2.scanner.Scanner#readNextTerminal} interface methods.
 */
public class Cup2PrivateTest {

  /** Creates a scanner conforming to the {@code cup2private.flex} specification. */
  private final ScannerFactory<Cup2PrivateScanner> scannerFactory =
      ScannerFactory.of(Cup2PrivateScanner::new);

  @Test
  public void readNextTerminal_isVisible() throws Exception {
    Cup2PrivateScanner scanner = scannerFactory.createScannerWithContent("Just checking cup2 !");
    assertEquals(Token.WORD, scanner.readNextTerminal().getSymbol());
    assertEquals(Token.WORD, scanner.readNextTerminal().getSymbol());
    assertEquals(Token.WORD, scanner.readNextTerminal().getSymbol());
    assertEquals(Token.OTHER, scanner.readNextTerminal().getSymbol());
    assertEquals(Token.EOF, scanner.readNextTerminal().getSymbol());
  }
}
