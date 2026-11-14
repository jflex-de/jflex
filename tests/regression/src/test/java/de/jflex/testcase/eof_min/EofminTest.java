/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eof_min;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Regression test for <a href="https://github.com/jflex-de/jflex/issues/82">#82 State minimizer
 * ignores EOF actions</a>.
 *
 * <p>The lexical states {@code YYINITIAL} and {@code STATE2} of the spec have the same rules and
 * differ only in their {@code <<EOF>>} action. The spec must not produce a warning for equivalent
 * lexical states, and the scanner must return the end of file value of the lexical state it is in.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/eof_min/eofmin.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/eof_min/eofmin.sysout",
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 3, lexical states 2",
      "entry: 0 0 0 0",
      "0: 1 -> 1; 2 -> 2",
      "1 [a0]:",
      "2 [a1]:",
      "a0: yybegin(STATE2);\\n          return 0;",
      "a1: yybegin(YYINITIAL);\\n          return 1;"
    },
    charClassesCount = 3,
    // everything else (no rule), a, b
    charClasses = {"{ [0-'`']['c'-1114111] }", "{ ['a'] }", "{ ['b'] }"})
public class EofminTest implements JFlexTest {

  /** {@code "a"}, switches to {@code STATE2} */
  private static final int A = 0;

  /** {@code "b"}, switches to {@code YYINITIAL} */
  private static final int B = 1;

  /** {@code <<EOF>>} in {@code YYINITIAL} */
  private static final int EOF_INITIAL = 2;

  /** {@code <<EOF>>} in {@code STATE2} */
  private static final int EOF_STATE2 = 3;

  private Eofmin scanner;

  /**
   * Checks the tokens of the input and the end of file value.
   *
   * @param input a string of {@code a} and {@code b}.
   * @param eof the expected end of file value.
   */
  private void assertScan(String input, int eof) throws IOException {
    scanner = new Eofmin(new StringReader(input));
    assertEquals(Eofmin.YYINITIAL, scanner.yystate());
    for (int i = 0; i < input.length(); i++) {
      String at = "at " + i + " of " + input;
      boolean isA = input.charAt(i) == 'a';
      assertEquals(isA ? A : B, scanner.yylex(), "rule " + at);
      assertEquals(input.substring(i, i + 1), scanner.yytext(), "text " + at);
      assertEquals(isA ? Eofmin.STATE2 : Eofmin.YYINITIAL, scanner.yystate(), "state " + at);
    }
    int state = scanner.yystate();
    // the value stays the same when the scanner is called again
    for (int i = 0; i < 3; i++) {
      assertEquals(eof, scanner.yylex(), "end of file value of " + input + ", call " + i);
      assertTrue(scanner.yyatEOF());
      assertEquals("", scanner.yytext());
      assertEquals(state, scanner.yystate());
    }
  }

  @Test
  public void aaa() throws Exception {
    assertScan("aaa", EOF_STATE2);
  }

  @Test
  public void ab() throws Exception {
    assertScan("ab", EOF_INITIAL);
  }

  @Test
  public void emptyInput() throws Exception {
    assertScan("", EOF_INITIAL);
  }

  @Test
  public void a() throws Exception {
    assertScan("a", EOF_STATE2);
  }

  @Test
  public void b() throws Exception {
    assertScan("b", EOF_INITIAL);
  }

  @Test
  public void ba() throws Exception {
    assertScan("ba", EOF_STATE2);
  }

  @Test
  public void aab() throws Exception {
    assertScan("aab", EOF_INITIAL);
  }
}
