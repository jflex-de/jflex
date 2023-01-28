/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.token_limit;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import java.io.EOFException;
import org.junit.Test;

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
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(-1);
  }

  @Test
  public void two_long_tokens() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("aaaaaaaaaaaaaaaa");
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(-1);
  }

  @Test
  public void two_long_tokens_separated() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("baaaaaaaabbaaaaaaaa");
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(-1);
  }

  @Test
  public void too_long() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("cccccccccc");
    try {
      scanner.yylex();
      assertThat(false).isTrue();
    } catch (EOFException e) {
      assertThat(e.getMessage()).isEqualTo("Scan buffer limit reached [8]");
    }
  }

  @Test
  public void too_long_later() throws Exception {
    Limit_Success scanner = scannerFactory.createScannerWithContent("aaaaaaaabbcccccccccc");
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(1);
    try {
      assertThat(scanner.yylex()).isEqualTo(3);
      assertThat(false).isTrue();
    } catch (EOFException e) {
      assertThat(e.getMessage()).isEqualTo("Scan buffer limit reached [8]");
    }
  }
}
