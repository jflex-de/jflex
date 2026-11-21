/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.empty_string;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * The empty string {@code ""} as part of a larger expression. The rules use distinct characters, so
 * that their automata are fully distinct.
 *
 * <p>The NFA of the empty string is a single state. Make sure this state is actually created and
 * not accidentally merged with the next expression to be inserted.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/empty_string/EmptyString.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 16,
    minimizedDfa = {
      "states 16, inputs 13, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 1; 3 -> 3; 4 -> 1; 5 -> 4; 6-10 -> 1; 11 -> 5; 12 -> 1",
      "1 [a0]:",
      "2 [a0]: 0-1 -> 6; 2 -> 7; 3-8 -> 6; 9 -> 8; 10-12 -> 6",
      "3 [a0]: 4 -> 9; 7 -> 10; 8 -> 11",
      "4 [a0]: 0-12 -> 12",
      "5 [a0]: 10 -> 13; 12 -> 14",
      "6: 0-1 -> 6; 2 -> 7; 3-12 -> 6",
      "7 [a1]: 0-1 -> 6; 2 -> 7; 3-12 -> 6",
      "8: 0-8 -> 6; 9 -> 8; 10-12 -> 6",
      "9 [a2]:",
      "10: 7 -> 10; 8 -> 11",
      "11: 4 -> 9",
      "12: 0-5 -> 12; 6 -> 15; 7-12 -> 12",
      "13: 12 -> 14",
      "14 [a3]:",
      "15 [a4]: 0-5 -> 12; 6 -> 15; 7-12 -> 12",
      "a0: return 0;",
      "a1: return 2;",
      "a2: return 1;",
      "a3: return 4;",
      "a4: return 3;"
    },
    charClassesCount = 13,
    charClasses = {
      "{ [0-''']['*'-';']['=']['?'-'Z']['\\']['^'-'`']['e'-'z']['|']['~'-1114111] }",
      "{ ['('] }",
      "{ [')'] }",
      "{ ['<'] }",
      "{ ['>'] }",
      "{ ['['] }",
      "{ [']'] }",
      "{ ['a'] }",
      "{ ['b'] }",
      "{ ['c'] }",
      "{ ['d'] }",
      "{ ['{'] }",
      "{ ['}'] }"
    })
public class EmptyStringTest implements JFlexTest {

  /** {@code "<" ("" | a* b) ">"} */
  private static final int FIRST_ALTERNATIVE = 1;

  /** {@code "(" ("" !(c+)) ")"} */
  private static final int BEFORE_NEGATION = 2;

  /** {@code "[" !"" "]"} */
  private static final int NEGATED = 3;

  /** {@code "{" (d | "") "" "}"} */
  private static final int AFTER_EXPRESSION = 4;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private EmptyString scanner;

  private void scan(String input) {
    scanner = new EmptyString(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(EmptyString.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  private void assertTokens(int rule, String... texts) throws IOException {
    scan(String.join("", texts));
    for (String text : texts) {
      assertToken(rule, text);
    }
    assertEof();
  }

  private void assertOnlyFallback(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1));
    }
    assertEof();
  }

  @Test
  public void firstAlternative() throws Exception {
    assertTokens(FIRST_ALTERNATIVE, "<>");
    assertTokens(FIRST_ALTERNATIVE, "<b>");
    assertTokens(FIRST_ALTERNATIVE, "<ab>");
    assertTokens(FIRST_ALTERNATIVE, "<aaab>");
    assertTokens(FIRST_ALTERNATIVE, "<>", "<ab>", "<>");
  }

  @Test
  public void firstAlternative_notInsideOtherAlternative() throws Exception {
    assertOnlyFallback("<a>");
    assertOnlyFallback("<aa>");
    assertOnlyFallback("<ba>");
    assertOnlyFallback("<bb>");
    assertOnlyFallback("<x>");
  }

  @Test
  public void beforeNegation() throws Exception {
    assertTokens(BEFORE_NEGATION, "()");
    assertTokens(BEFORE_NEGATION, "(d)");
    assertTokens(BEFORE_NEGATION, "(cd)");
    assertTokens(BEFORE_NEGATION, "(dc)");
    assertTokens(BEFORE_NEGATION, "(ccdcc)");
    assertTokens(BEFORE_NEGATION, "(c))");
  }

  @Test
  public void beforeNegation_negatedExpression() throws Exception {
    assertOnlyFallback("(c)");
    assertOnlyFallback("(cc)");
    assertOnlyFallback("(cccc)");
  }

  @Test
  public void negated() throws Exception {
    assertTokens(NEGATED, "[x]");
    assertTokens(NEGATED, "[xy]");
    assertTokens(NEGATED, "[\n]");
    assertTokens(NEGATED, "[]]");
    assertTokens(NEGATED, "[[]");
    assertOnlyFallback("[]");
    assertOnlyFallback("[");
  }

  @Test
  public void afterExpression() throws Exception {
    assertTokens(AFTER_EXPRESSION, "{}");
    assertTokens(AFTER_EXPRESSION, "{d}");
    assertTokens(AFTER_EXPRESSION, "{d}", "{}", "{d}");
    assertOnlyFallback("{dd}");
    assertOnlyFallback("{x}");
    assertOnlyFallback("{");
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
