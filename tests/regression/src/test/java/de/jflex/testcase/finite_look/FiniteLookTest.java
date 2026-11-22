/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.finite_look;

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
 * Lookahead expressions {@code r1 / r2} where {@code r2} is a choice of expressions with fixed
 * lengths, and multiple choices match.
 *
 * <p>Test that the match is the longest match of {@code r1}, independent of the order of the
 * choices. Also test that no rule gets a never-match warning for a choice that is not needed
 * (lookahead choice is rule-internal).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/finite_look/FiniteLook.flex",
    warnings = @Warn(value = ErrorMessages.NEVER_MATCH, line = 35),
    quiet = true,
    minimizedDfaStatesCount = 23,
    minimizedDfa = {
      "states 23, inputs 13, lexical states 1",
      "entry: 0 0",
      "0: 0-2 -> 1; 3 -> 2; 4 -> 3; 5 -> 4; 6 -> 5; 7 -> 6; 8 -> 7; 9 -> 2; 10 -> 8; 11 -> 9; 12 ->"
          + " 1",
      "1 [a0]:",
      "2 [a0]: 1-2 -> 10; 3 -> 11; 9 -> 12",
      "3 [a1]: 4 -> 3",
      "4 [a2]: 5 -> 4",
      "5 [a0]: 6 -> 13",
      "6 [a0]: 7 -> 14",
      "7 [a3]: 8 -> 7",
      "8 [a0]: 1-2 -> 15; 3 -> 16; 10 -> 17",
      "9 [a0]: 11 -> 18; 12 -> 19",
      "10 [a4]:",
      "11 [a4]: 1-2 -> 10; 3 -> 11; 9 -> 12",
      "12: 1-2 -> 10; 3 -> 11; 9 -> 12",
      "13 [a5]: 6 -> 13",
      "14 [a6]: 7 -> 14",
      "15 [a7]:",
      "16 [a7]: 1 -> 20",
      "17: 1-2 -> 15; 3 -> 16; 10 -> 17",
      "18: 11 -> 18; 12 -> 19",
      "19 [a8]: 12 -> 21",
      "20 [a9]:",
      "21 [a10]: 12 -> 22",
      "22 [a11]:",
      "a0: return 0;",
      "a1 FINITE_CHOICE: return 1;",
      "a2 FINITE_CHOICE: return 2;",
      "a3 FINITE_CHOICE: return 5;",
      "a4 FINITE_CHOICE len 1: return 8;",
      "a5 FINITE_CHOICE len 1: return 3;",
      "a6 FINITE_CHOICE len 1: return 4;",
      "a7 FINITE_CHOICE len 1: return 9;",
      "a8 FINITE_CHOICE len 1: return 6;",
      "a9 FINITE_CHOICE len 2: return 9;",
      "a10 FINITE_CHOICE len 2: return 6;",
      "a11 FINITE_CHOICE len 2: return 7;"
    },
    charClassesCount = 13,
    charClasses = {
      "{ [0-9][14-'`']['h'-'w']['z'-132][134-8231][8234-1114111] }",
      "{ [10] }",
      "{ [11-12][133][8232-8233] }",
      "{ [13] }",
      "{ ['a'] }",
      "{ ['b'] }",
      "{ ['c'] }",
      "{ ['d'] }",
      "{ ['e'] }",
      "{ ['f'] }",
      "{ ['g'] }",
      "{ ['x'] }",
      "{ ['y'] }"
    })
public class FiniteLookTest implements JFlexTest {

  /** {@code "a"+ / ("" | "a")} */
  private static final int EMPTY_FIRST = 1;

  /** {@code "b"+ / ("b" | "")} */
  private static final int EMPTY_LAST = 2;

  /** {@code "c"+ / ("c" | "cc")} */
  private static final int SHORT_FIRST = 3;

  /** {@code "d"+ / ("dd" | "d")} */
  private static final int SHORT_LAST = 4;

  /** {@code "e"+ / ("ee" | "" | "e")} */
  private static final int THREE_CHOICES = 5;

  /** {@code "x"+ / ("yy" | "y")} */
  private static final int FIRST_RULE = 6;

  /** {@code "x"+ "y" / ("" | "y" | "yy")} */
  private static final int SECOND_RULE = 7;

  /** {@code [f\r]+$} */
  private static final int EOL_WITH_CR = 8;

  /** {@code "g"+$} */
  private static final int EOL = 9;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private FiniteLook scanner;

  private void scan(String input) {
    scanner = new FiniteLook(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(FiniteLook.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void emptyChoiceFirst() throws Exception {
    scan("a");
    assertToken(EMPTY_FIRST, "a");
    assertEof();

    scan("aa");
    assertToken(EMPTY_FIRST, "aa");
    assertEof();

    scan("aaab");
    assertToken(EMPTY_FIRST, "aaa");
    assertToken(EMPTY_LAST, "b");
    assertEof();
  }

  @Test
  public void emptyChoiceLast() throws Exception {
    scan("b");
    assertToken(EMPTY_LAST, "b");
    assertEof();

    scan("bbba");
    assertToken(EMPTY_LAST, "bbb");
    assertToken(EMPTY_FIRST, "a");
    assertEof();
  }

  @Test
  public void shortChoiceFirst() throws Exception {
    scan("cc");
    assertToken(SHORT_FIRST, "c");
    assertToken(OTHER, "c");
    assertEof();

    scan("cccc");
    assertToken(SHORT_FIRST, "ccc");
    assertToken(OTHER, "c");
    assertEof();
  }

  @Test
  public void shortChoiceLast() throws Exception {
    scan("dd");
    assertToken(SHORT_LAST, "d");
    assertToken(OTHER, "d");
    assertEof();

    scan("dddd");
    assertToken(SHORT_LAST, "ddd");
    assertToken(OTHER, "d");
    assertEof();
  }

  @Test
  public void threeChoices() throws Exception {
    scan("e");
    assertToken(THREE_CHOICES, "e");
    assertEof();

    scan("eee");
    assertToken(THREE_CHOICES, "eee");
    assertEof();
  }

  /** The lookahead does not match, so the rule does not. */
  @Test
  public void noLookahead() throws Exception {
    scan("c");
    assertToken(OTHER, "c");
    assertEof();

    scan("x");
    assertToken(OTHER, "x");
    assertEof();
  }

  /** Both rules match {@code xyy}. The first one takes precedence despite longer lookahead. */
  @Test
  public void rulePriority() throws Exception {
    scan("xyy");
    assertToken(FIRST_RULE, "x");
    assertToken(OTHER, "y");
    assertToken(OTHER, "y");
    assertEof();
  }

  /** The second rule has the longer match including the lookahead. */
  @Test
  public void longestMatchWithLookahead() throws Exception {
    scan("xxyyy");
    assertToken(SECOND_RULE, "xxy");
    assertToken(OTHER, "y");
    assertToken(OTHER, "y");
    assertEof();
  }

  /** Before {@code \r\n} an expression that can match {@code \r} includes {@code \r}. */
  @Test
  public void endOfLine_crInExpression() throws Exception {
    scan("ff\r\n");
    assertToken(EOL_WITH_CR, "ff\r");
    assertToken(OTHER, "\n");
    assertEof();

    scan("f\r\r");
    assertToken(EOL_WITH_CR, "f\r");
    assertToken(OTHER, "\r");
    assertEof();

    scan("f\n");
    assertToken(EOL_WITH_CR, "f");
    assertToken(OTHER, "\n");
    assertEof();
  }

  @Test
  public void endOfLine() throws Exception {
    scan("gg\r\n");
    assertToken(EOL, "gg");
    assertToken(EOL_WITH_CR, "\r");
    assertToken(OTHER, "\n");
    assertEof();

    scan("g ");
    assertToken(EOL, "g");
    assertToken(OTHER, " ");
    assertEof();

    scan("g");
    assertToken(OTHER, "g");
    assertEof();
  }
}
