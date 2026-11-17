/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.token_limit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import de.jflex.util.scanner.ScannerFactory;
import java.io.EOFException;
import org.junit.jupiter.api.Test;

/**
 * Test success cases for {@code %token_size_limit} directive.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/issues/197">#197</a>.
 */
public class LimitTest0 {
  private final ScannerFactory<Limit_Success> scannerFactory =
      ScannerFactory.of(Limit_Success::new);

  @Test
  public void only_long_token() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("aaaaaaaa");
    assertEquals(0, scanner.yylex());
    assertEquals(-1, scanner.yylex());
  }

  @Test
  public void two_long_tokens() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("aaaaaaaaaaaaaaaa");
    assertEquals(0, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(-1, scanner.yylex());
  }

  @Test
  public void two_long_tokens_separated() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("baaaaaaaabbaaaaaaaa");
    assertEquals(1, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(0, scanner.yylex());
    assertEquals(-1, scanner.yylex());
  }

  @Test
  public void too_long() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("cccccccccc");
    try {
      scanner.yylex();
      fail();
    } catch (EOFException e) {
      assertEquals("Scan buffer limit reached [8]", e.getMessage());
    }
  }

  @Test
  public void too_long_later() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("aaaaaaaabbcccccccccc");
    assertEquals(0, scanner.yylex());
    assertEquals(1, scanner.yylex());
    assertEquals(1, scanner.yylex());
    try {
      assertEquals(3, scanner.yylex());
      fail();
    } catch (EOFException e) {
      assertEquals("Scan buffer limit reached [8]", e.getMessage());
    }
  }
}
