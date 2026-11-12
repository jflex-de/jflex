/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for bug <a href="https://github.com/jflex-de/jflex/issues/74">#74 (yytext() return longer
 * than expected with lookahead)</a>
 *
 * <p>(this tests fixed lookahead/base only)
 *
 * <p>the pattern ab / cde { System.out.println(yytext()); return 2; } abcd / f {
 * System.out.println(yytext()); return 7; }
 *
 * <p>with string abcde
 *
 * <p>prints out "abcd", but returns 2.
 *
 * <p>Also a test for the format of the {@code %debug} trace with {@code %line} and {@code %column}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/look/look.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/look/look.sysout",
    quiet = true,
    minimizedDfaStatesCount = 8,
    // The DFA inputs are the character classes below.
    minimizedDfa = {
      "states 8, inputs 7, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2-6 -> 1",
      "1 [a0]:",
      "2 [a0]: 2 -> 3",
      "3: 3 -> 4",
      "4: 4 -> 5",
      "5: 5 -> 6; 6 -> 7",
      "6 [a1]:",
      "7 [a2]:",
      "a0: return 0;",
      "a1 FIXED_BASE len 2: return 2;",
      "a2 FIXED_BASE len 4: return 7;"
    },
    charClassesCount = 7,
    charClasses = {
      "{ [0-'`']['g'-1114111] }",
      "{ ['a'] }",
      "{ ['b'] }",
      "{ ['c'] }",
      "{ ['d'] }",
      "{ ['e'] }",
      "{ ['f'] }"
    })
public class LookTest implements JFlexTest {

  /** {@code "ab"/"cde"} */
  private static final int AB = 2;

  /** {@code "abcd"/"f"} */
  private static final int ABCD = 7;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private Look scanner;

  private void scan(String input) {
    scanner = new Look(new StringReader(input));
  }

  /** Checks the next token: the matched rule + text and position (from 0). */
  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Look.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** The text of the first rule is {@code ab}, not {@code abcd} (#74). */
  @Test
  public void shortBase() throws Exception {
    scan("abcde\n");
    assertToken(AB, "ab", 0, 0);
    // the lookahead is scanned again
    assertToken(OTHER, "c", 0, 2);
    assertToken(OTHER, "d", 0, 3);
    assertToken(OTHER, "e", 0, 4);
    assertToken(OTHER, "\n", 0, 5);
    assertEof();
  }

  @Test
  public void longBase() throws Exception {
    scan("abcdf");
    assertToken(ABCD, "abcd", 0, 0);
    assertToken(OTHER, "f", 0, 4);
    assertEof();
  }

  @Test
  public void bothRules() throws Exception {
    scan("ababcdfabcde\n");
    // "ab" is not followed by "cde"
    assertToken(OTHER, "a", 0, 0);
    assertToken(OTHER, "b", 0, 1);
    assertToken(ABCD, "abcd", 0, 2);
    assertToken(OTHER, "f", 0, 6);
    assertToken(AB, "ab", 0, 7);
    assertToken(OTHER, "c", 0, 9);
    assertToken(OTHER, "d", 0, 10);
    assertToken(OTHER, "e", 0, 11);
    assertToken(OTHER, "\n", 0, 12);
    assertEof();
  }

  @Test
  public void secondLine() throws Exception {
    scan("f\nabcdf\nabcde");
    assertToken(OTHER, "f", 0, 0);
    assertToken(OTHER, "\n", 0, 1);
    assertToken(ABCD, "abcd", 1, 0);
    assertToken(OTHER, "f", 1, 4);
    assertToken(OTHER, "\n", 1, 5);
    assertToken(AB, "ab", 2, 0);
    assertToken(OTHER, "c", 2, 2);
    assertToken(OTHER, "d", 2, 3);
    assertToken(OTHER, "e", 2, 4);
    assertEof();
  }

  /** At the end of input, an incomplete lookahead matches neither rule. */
  @Test
  public void lookaheadCutOff() throws Exception {
    scan("abcd");
    assertToken(OTHER, "a", 0, 0);
    assertToken(OTHER, "b", 0, 1);
    assertToken(OTHER, "c", 0, 2);
    assertToken(OTHER, "d", 0, 3);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }

  /**
   * The {@code %debug} trace: position counted from 1, the matched text without the lookahead, and
   * the line in the spec and the code of the action.
   */
  @Test
  public void debugTrace() throws Exception {
    ByteArrayOutputStream trace = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(trace, true, "UTF-8"))) {
      scan("abcdf\nabcde");
      while (scanner.yylex() != Look.YYEOF) {}
    }
    String expected =
        String.join(
            System.lineSeparator(),
            "line: 1 col: 1 match: --abcd--",
            "action [35] { return 7; }",
            "line: 1 col: 5 match: --f--",
            "action [37] { return 0; }",
            "line: 1 col: 6 match: --\\u000A--",
            "action [37] { return 0; }",
            "line: 2 col: 1 match: --ab--",
            "action [34] { return 2; }",
            "line: 2 col: 3 match: --c--",
            "action [37] { return 0; }",
            "line: 2 col: 4 match: --d--",
            "action [37] { return 0; }",
            "line: 2 col: 5 match: --e--",
            "action [37] { return 0; }",
            "");
    assertEquals(expected, trace.toString("UTF-8"));
  }
}
