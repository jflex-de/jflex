/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.token_limit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test success cases for {@code %token_size_limit} directive.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/issues/197">#197</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/token_limit/limit_success.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 20,
    charClassesCount = 4,
    charClasses = {"{ [0-'`']['d'-1114111] }", "{ ['a'] }", "{ ['b'] }", "{ ['c'] }"})
public class LimitTest0 implements JFlexTest {

  /** The longest token that fits the limit: {@code "a"{8}} */
  private static final int LONG = 0;

  /** {@code "b"} */
  private static final int SHORT = 1;

  private static final String LONG_TOKEN = "aaaaaaaa";

  private Limit_Success scanner;

  private void scan(String input) {
    scanner = new Limit_Success(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(Limit_Success.YYEOF, scanner.yylex());
  }

  private void assertLimitReached() {
    EOFException e = assertThrows(EOFException.class, () -> scanner.yylex());
    assertEquals("Scan buffer limit reached [8]", e.getMessage());
  }

  @Test
  public void only_long_token() throws Exception {
    scan("aaaaaaaa");
    assertToken(LONG, LONG_TOKEN);
    assertEof();
  }

  @Test
  public void two_long_tokens() throws Exception {
    scan("aaaaaaaaaaaaaaaa");
    assertToken(LONG, LONG_TOKEN);
    assertToken(LONG, LONG_TOKEN);
    assertEof();
  }

  @Test
  public void two_long_tokens_separated() throws Exception {
    scan("baaaaaaaabbaaaaaaaa");
    assertToken(SHORT, "b");
    assertToken(LONG, LONG_TOKEN);
    assertToken(SHORT, "b");
    assertToken(SHORT, "b");
    assertToken(LONG, LONG_TOKEN);
    assertEof();
  }

  @Test
  public void too_long() throws Exception {
    scan("cccccccccc");
    assertLimitReached();
  }

  @Test
  public void too_long_later() throws Exception {
    scan("aaaaaaaabbcccccccccc");
    assertToken(LONG, LONG_TOKEN);
    assertToken(SHORT, "b");
    assertToken(SHORT, "b");
    assertLimitReached();
  }
}
