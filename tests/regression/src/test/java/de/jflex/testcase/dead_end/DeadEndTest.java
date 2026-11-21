/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dead_end;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.IOException;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * Test a transition that leads into a dead end. In {@code "ab" [] "c"} nothing can follow {@code
 * ab}.
 *
 * <p>The DFA has no transition on {@code b} after {@code a}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/dead_end/DeadEnd.flex",
    warnings = @Warn(value = ErrorMessages.NEVER_MATCH, line = 19),
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 4, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2-3 -> 1",
      "1 [a0]:",
      "2 [a1]:",
      "a0: return 0;",
      "a1: return 2;"
    },
    charClassesCount = 4,
    charClasses = {"{ [0-'`']['d'-1114111] }", "{ ['a'] }", "{ ['b'] }", "{ ['c'] }"})
public class DeadEndTest implements JFlexTest {

  /** {@code "a"} */
  private static final int A = 2;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private DeadEnd scanner;

  private void scan(String input) {
    scanner = new DeadEnd(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(DeadEnd.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void beforeDeadEnd() throws Exception {
    scan("ab");
    assertToken(A, "a");
    assertToken(OTHER, "b");
    assertEof();
  }

  /** The rule with the dead end does not match. */
  @Test
  public void afterDeadEnd() throws Exception {
    scan("abc");
    assertToken(A, "a");
    assertToken(OTHER, "b");
    assertToken(OTHER, "c");
    assertEof();
  }

  @Test
  public void otherRules() throws Exception {
    scan("aax");
    assertToken(A, "a");
    assertToken(A, "a");
    assertToken(OTHER, "x");
    assertEof();
  }
}
