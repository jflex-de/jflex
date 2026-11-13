/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eol_look;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test for lookahead expressions {@code r1 / r2} where neither side has a fixed length, but {@code
 * r2} is a finite choice of fixed lengths, in particular the end of line operator {@code $}.
 *
 * <p>The rule {@code "bb"$} can never match, because {@code "bb"|"z" $} is before it. The spec has
 * to produce this warning (and only this one).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/eol_look/eol.flex",
    // the never-match warning for "bb"$
    sysout = "src/test/java/de/jflex/testcase/eol_look/eol.sysout",
    quiet = true,
    minimizedDfaStatesCount = 16,
    minimizedDfa = {
      "states 16, inputs 10, lexical states 1",
      "entry: 0 0",
      "0: 0-3 -> 1; 4 -> 2; 5 -> 3; 6 -> 4; 7-8 -> 1; 9 -> 5",
      "1 [a0]:",
      "2 [a0]: 4 -> 6; 7 -> 7; 9 -> 8",
      "3 [a0]: 5 -> 9",
      "4 [a1]:",
      "5 [a0]: 1-2 -> 10; 3 -> 11",
      "6: 7 -> 7; 9 -> 8",
      "7 [a2]: 8 -> 12",
      "8: 8 -> 12; 9 -> 13",
      "9: 1-2 -> 10; 3 -> 11",
      "10 [a3]:",
      "11 [a3]: 1 -> 14",
      "12 [a4]:",
      "13: 9 -> 15",
      "14 [a5]:",
      "15 [a6]:",
      "a0: return 0;",
      "a1: /* blah */ return 4;",
      "a2 FINITE_CHOICE len 1: /* normal */ return 1;",
      "a3 FINITE_CHOICE len 1: /* eol */ return 2;",
      "a4 FINITE_CHOICE len 2: /* normal */ return 1;",
      "a5 FINITE_CHOICE len 2: /* eol */ return 2;",
      "a6 FINITE_CHOICE len 3: /* normal */ return 1;"
    },
    charClassesCount = 10,
    charClasses = {
      // 0: everything else
      "{ [0-9][14-'`']['d'-'w']['{'-132][134-8231][8234-1114111] }",
      // 1: single \n because it can follow \r in a line end
      "{ [10] }",
      // 2: the line ends \u000B \u000C \u0085 \u2028 \u2029
      "{ [11-12][133][8232-8233] }",
      // 3: single \r as above for \n
      "{ [13] }",
      // 4 to 9: the letters of the rules
      "{ ['a'] }",
      "{ ['b'] }",
      "{ ['c'] }",
      "{ ['x'] }",
      "{ ['y'] }",
      "{ ['z'] }"
    })
public class EolTest implements JFlexTest {

  /** {@code "aa"|"a" / "x"|"xy"|"zzz"|"zy"} */
  private static final int NORMAL = 1;

  /** {@code "bb"|"z" $} */
  private static final int EOL = 2;

  /** {@code "c"} */
  private static final int C = 4;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private Eol scanner;

  private void scan(String input) {
    scanner = new Eol(new StringReader(input));
  }

  /**
   * Checks the next token: the matched rule + text and position (from 0). The text and its length
   * are without the lookahead.
   */
  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Eol.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Checks that the scanner matches every character of the one-line input with {@code [^]}. */
  private void assertOnlyFallback(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1), 0, i);
    }
    assertEof();
  }

  /**
   * Checks {@code $} before a line end of one character: both alternatives of the base match, the
   * line end is not part of the text, and it is scanned again as the next token.
   */
  private void assertEndOfLine(String lineEnd) throws IOException {
    scan("z" + lineEnd + "bb" + lineEnd + "zbb");
    assertToken(EOL, "z", 0, 0);
    assertToken(OTHER, lineEnd, 0, 1);
    assertToken(EOL, "bb", 1, 0);
    assertToken(OTHER, lineEnd, 1, 2);
    // not at the end of a line
    assertToken(OTHER, "z", 2, 0);
    assertToken(OTHER, "b", 2, 1);
    assertToken(OTHER, "b", 2, 2);
    assertEof();
  }

  // --- the lookahead "x"|"xy"|"zzz"|"zy"

  @Test
  public void lookaheadOfLength1() throws Exception {
    scan("ax");
    assertToken(NORMAL, "a", 0, 0);
    assertToken(OTHER, "x", 0, 1);
    assertEof();
  }

  /** {@code axy} matches with the lookaheads {@code x} and {@code xy}; the text is the same. */
  @Test
  public void lookaheadOfLength1Or2() throws Exception {
    scan("axy");
    assertToken(NORMAL, "a", 0, 0);
    assertToken(OTHER, "x", 0, 1);
    assertToken(OTHER, "y", 0, 2);
    assertEof();
  }

  @Test
  public void lookaheadOfLength2() throws Exception {
    scan("azy");
    assertToken(NORMAL, "a", 0, 0);
    assertToken(OTHER, "z", 0, 1);
    assertToken(OTHER, "y", 0, 2);
    assertEof();
  }

  @Test
  public void lookaheadOfLength3() throws Exception {
    scan("azzz");
    assertToken(NORMAL, "a", 0, 0);
    assertToken(OTHER, "z", 0, 1);
    assertToken(OTHER, "z", 0, 2);
    assertToken(OTHER, "z", 0, 3);
    assertEof();
  }

  /** No alternative of the lookahead matches. */
  @Test
  public void incompleteLookahead() throws Exception {
    assertOnlyFallback("azz");
    assertOnlyFallback("azzx");
    assertOnlyFallback("az");
    assertOnlyFallback("ay");
    assertOnlyFallback("a");
    assertOnlyFallback("aa");
  }

  // --- the base "aa"|"a"

  @Test
  public void longBase() throws Exception {
    scan("aax");
    assertToken(NORMAL, "aa", 0, 0);
    assertToken(OTHER, "x", 0, 2);
    assertEof();
  }

  @Test
  public void longBaseBeforeEachLookahead() throws Exception {
    scan("aaxyaazzzaazy");
    assertToken(NORMAL, "aa", 0, 0);
    assertToken(OTHER, "x", 0, 2);
    assertToken(OTHER, "y", 0, 3);
    assertToken(NORMAL, "aa", 0, 4);
    assertToken(OTHER, "z", 0, 6);
    assertToken(OTHER, "z", 0, 7);
    assertToken(OTHER, "z", 0, 8);
    assertToken(NORMAL, "aa", 0, 9);
    assertToken(OTHER, "z", 0, 11);
    assertToken(OTHER, "y", 0, 12);
    assertEof();
  }

  /** The first {@code a} of {@code aaax} is followed by neither the lookahead nor {@code ax}. */
  @Test
  public void baseIsAtMostTwoA() throws Exception {
    scan("aaax");
    assertToken(OTHER, "a", 0, 0);
    assertToken(NORMAL, "aa", 0, 1);
    assertToken(OTHER, "x", 0, 3);
    assertEof();
  }

  // --- $

  @Test
  public void endOfLineLf() throws Exception {
    assertEndOfLine("\n");
  }

  @Test
  public void endOfLineCr() throws Exception {
    assertEndOfLine("\r");
  }

  /**
   * Before {@code \r\n} the lookahead has length 2. Both characters are scanned again, one by one.
   * The {@code \n} is still in the line that it ends together with {@code \r}. Check {@link
   * #columnOfLfAfterCr()} for the column, which might be a bug.
   */
  @Test
  public void endOfLineCrLf() throws Exception {
    scan("z\r\nbb\r\nz");
    assertToken(EOL, "z", 0, 0);
    assertToken(OTHER, "\r", 0, 1);
    assertLfInLine(0);
    assertToken(EOL, "bb", 1, 0);
    assertToken(OTHER, "\r", 1, 2);
    assertLfInLine(1);
    assertToken(OTHER, "z", 2, 0);
    assertEof();
  }

  /** Checks the next token to be a {@code \n} in the given line, without its column. */
  private void assertLfInLine(int line) throws IOException {
    assertEquals(OTHER, scanner.yylex(), "rule of \\n in line " + line);
    assertEquals("\n", scanner.yytext());
    assertEquals(1, scanner.yylength());
    assertEquals(line, scanner.line());
  }

  /**
   * The column of a {@code \n} that is matched on its own after a {@code \r}. Line counting looks
   * ahead for the {@code \n}, but the column is already at 0.
   */
  @Disabled("TODO suspected bug: yycolumn of a \\n matched on its own after \\r is 0, not 2")
  @Test
  public void columnOfLfAfterCr() throws Exception {
    scan("z\r\n");
    assertToken(EOL, "z", 0, 0);
    assertToken(OTHER, "\r", 0, 1);
    assertToken(OTHER, "\n", 0, 2);
    assertEof();
  }

  @Test
  public void endOfLineNextLine() throws Exception {
    assertEndOfLine("\u0085");
  }

  @Test
  public void endOfLineLineSeparator() throws Exception {
    assertEndOfLine("\u2028");
  }

  @Test
  public void endOfLineParagraphSeparator() throws Exception {
    assertEndOfLine("\u2029");
  }

  @Test
  public void endOfLineVerticalTab() throws Exception {
    assertEndOfLine("\u000B");
  }

  @Test
  public void endOfLineFormFeed() throws Exception {
    assertEndOfLine("\u000C");
  }

  /** {@code $} is a lookahead for a line end, so the end of input is not an end of line. */
  @Test
  public void endOfInputIsNoEndOfLine() throws Exception {
    assertOnlyFallback("z");
    assertOnlyFallback("bb");
    assertOnlyFallback("zbbz");
  }

  /** Other white space and the neighbours of the line ends in Unicode are no end of line. */
  @Test
  public void noEndOfLine() throws Exception {
    assertOnlyFallback("z bb\tz\u0084bb\u0086z\u2027bb\u202Az\u000Ebbz");
  }

  /** Only the last {@code z} or {@code bb} of a line matches. */
  @Test
  public void lastOfLine() throws Exception {
    scan("zz\nbbb\nbbbb\n");
    assertToken(OTHER, "z", 0, 0);
    assertToken(EOL, "z", 0, 1);
    assertToken(OTHER, "\n", 0, 2);
    assertToken(OTHER, "b", 1, 0);
    assertToken(EOL, "bb", 1, 1);
    assertToken(OTHER, "\n", 1, 3);
    assertToken(OTHER, "b", 2, 0);
    assertToken(OTHER, "b", 2, 1);
    assertToken(EOL, "bb", 2, 2);
    assertToken(OTHER, "\n", 2, 4);
    assertEof();
  }

  /** The lookahead {@code zzz} is scanned again, and its last {@code z} is at the end of a line. */
  @Test
  public void lookaheadIsScannedAgain() throws Exception {
    scan("azzz\n");
    assertToken(NORMAL, "a", 0, 0);
    assertToken(OTHER, "z", 0, 1);
    assertToken(OTHER, "z", 0, 2);
    assertToken(EOL, "z", 0, 3);
    assertToken(OTHER, "\n", 0, 4);
    assertEof();
  }

  // --- the other rules

  /** The rule {@code "c"} was lost in a comment that was not closed. */
  @Test
  public void ruleC() throws Exception {
    scan("cac\n");
    assertToken(C, "c", 0, 0);
    assertToken(OTHER, "a", 0, 1);
    assertToken(C, "c", 0, 2);
    assertToken(OTHER, "\n", 0, 3);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }

  /** The input of the former golden file test. */
  @Test
  public void severalLines() throws Exception {
    scan("xzyaaxaxyadfazzzbcdebb\nbb\nblaazyaz\n\n");
    assertToken(OTHER, "x", 0, 0);
    assertToken(OTHER, "z", 0, 1);
    assertToken(OTHER, "y", 0, 2);
    assertToken(NORMAL, "aa", 0, 3);
    assertToken(OTHER, "x", 0, 5);
    assertToken(NORMAL, "a", 0, 6);
    assertToken(OTHER, "x", 0, 7);
    assertToken(OTHER, "y", 0, 8);
    assertToken(OTHER, "a", 0, 9);
    assertToken(OTHER, "d", 0, 10);
    assertToken(OTHER, "f", 0, 11);
    assertToken(NORMAL, "a", 0, 12);
    assertToken(OTHER, "z", 0, 13);
    assertToken(OTHER, "z", 0, 14);
    assertToken(OTHER, "z", 0, 15);
    assertToken(OTHER, "b", 0, 16);
    assertToken(C, "c", 0, 17);
    assertToken(OTHER, "d", 0, 18);
    assertToken(OTHER, "e", 0, 19);
    assertToken(EOL, "bb", 0, 20);
    assertToken(OTHER, "\n", 0, 22);
    assertToken(EOL, "bb", 1, 0);
    assertToken(OTHER, "\n", 1, 2);
    assertToken(OTHER, "b", 2, 0);
    assertToken(OTHER, "l", 2, 1);
    assertToken(NORMAL, "aa", 2, 2);
    assertToken(OTHER, "z", 2, 4);
    assertToken(OTHER, "y", 2, 5);
    assertToken(OTHER, "a", 2, 6);
    assertToken(EOL, "z", 2, 7);
    assertToken(OTHER, "\n", 2, 8);
    assertToken(OTHER, "\n", 3, 0);
    assertEof();
  }
}
