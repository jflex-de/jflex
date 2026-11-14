/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot_jlex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests dot ({@code .}) with JLex semantics ({@code --jlex}). Should match every character apart
 * from LF and unpaired surrogates.
 *
 * <p>Without {@code --jlex}, . does not match other line terminators, see {@code DotnewlineTest}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/dot_jlex/dotjlex.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/dot_jlex/dotjlex.sysout",
    quiet = true,
    jlexCompat = true,
    minimizedDfaStatesCount = 4,
    minimizedDfa = {
      "states 4, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 3",
      "1 [a0]:",
      "2 [a1]:",
      "3 [a2]:",
      "a0: return 1;",
      "a1: return 2;",
      "a2: /* unpaired surrogate */ return 3;"
    },
    charClassesCount = 3,
    charClasses = {"{ [0-9][11-55295][57344-1114111] }", "{ [10] }", "{ [55296-57343] }"})
public class DotJlexTest implements JFlexTest {

  /** {@code .} */
  private static final int DOT = 1;

  /** {@code \n} */
  private static final int LF = 2;

  /** {@code [^]}, which is left with the unpaired surrogates. */
  private static final int SURROGATE = 3;

  private Dotjlex scanner;

  private void scan(String input) {
    scanner = new Dotjlex(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(Dotjlex.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void dot_matchesCr() throws Exception {
    scan("a\rb");
    assertToken(DOT, "a");
    assertToken(DOT, "\r");
    assertToken(DOT, "b");
    assertEof();
  }

  /** CR LF are two matches: dot for CR, then LF. */
  @Test
  public void dot_crLf() throws Exception {
    scan("\r\n\n\r");
    assertToken(DOT, "\r");
    assertToken(LF, "\n");
    assertToken(LF, "\n");
    assertToken(DOT, "\r");
    assertEof();
  }

  /** VT, FF, U+0085, U+2028 and U+2029 */
  @Test
  public void dot_matchesOtherLineTerminators() throws Exception {
    scan("\u000B\u000C\u0085\u2028\u2029");
    assertToken(DOT, "\u000B");
    assertToken(DOT, "\u000C");
    assertToken(DOT, "\u0085");
    assertToken(DOT, "\u2028");
    assertToken(DOT, "\u2029");
    assertEof();
  }

  @Test
  public void dot_doesNotMatchLf() throws Exception {
    scan("a\nb");
    assertToken(DOT, "a");
    assertToken(LF, "\n");
    assertToken(DOT, "b");
    assertEof();
  }

  /** As without {@code --jlex}, dot matches a surrogate pair, but no unpaired surrogate. */
  @Test
  public void dot_doesNotMatchUnpairedSurrogates() throws Exception {
    scan("\uDC00\uD800a\uD800\uDC00");
    assertToken(SURROGATE, "\uDC00");
    assertToken(SURROGATE, "\uD800");
    assertToken(DOT, "a");
    assertToken(DOT, "\uD800\uDC00");
    assertEof();
  }
}
