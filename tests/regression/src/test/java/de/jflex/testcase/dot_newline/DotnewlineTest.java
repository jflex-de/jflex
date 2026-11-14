/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot_newline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests that dot ({@code .}) matches every character but the line terminators LF, VT, FF, CR,
 * U+0085, U+2028 and U+2029 and the unpaired surrogates, and that {@code \R} matches these line
 * terminators and CR LF.
 *
 * <p>The last rule of the spec is {@code [^]}. It can never match, because the rest already covers
 * everything. Check for warning.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/dot_newline/dotnewline.flex",
    // the never-match warning for [^]
    sysout = "src/test/java/de/jflex/testcase/dot_newline/dotnewline.sysout",
    quiet = true,
    minimizedDfaStatesCount = 5,
    minimizedDfa = {
      "states 5, inputs 5, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1-2 -> 2; 3 -> 3; 4 -> 4",
      "1 [a0]:",
      "2 [a1]:",
      "3 [a1]: 1 -> 2",
      "4 [a2]:",
      "a0: return 1;",
      "a1: return 2;",
      "a2: /* unpaired surrogate */ return 3;"
    },
    charClassesCount = 5,
    charClasses = {
      "{ [0-9][14-132][134-8231][8234-55295][57344-1114111] }",
      "{ [10] }",
      "{ [11-12][133][8232-8233] }",
      "{ [13] }",
      "{ [55296-57343] }"
    },
    sameCharClass = {
      "\u0000\t\u000E\u0084\u0086\u2027\u202A\uD7FF\uE000\uFFFD\uFFFE\uFFFF"
          + "\uD800\uDC00\uDBFF\uDFFF",
      "\u000B\u000C\u0085\u2028\u2029",
      // the first and last low and high surrogate. Low before high, so that they are not pairs.
      "\uDC00\uDFFF\uD800\uDBFF"
    },
    distinctCharClasses = {"a\n\r\u0085\uDC00"})
public class DotnewlineTest implements JFlexTest {

  /** {@code .} */
  private static final int DOT = 1;

  /** {@code \R} */
  private static final int NEWLINE = 2;

  /** The surrogates, U+D800 to U+DFFF. */
  private static final int SURROGATE = 3;

  private Dotnewline scanner;

  private void scan(String input) {
    scanner = new Dotnewline(new StringReader(input));
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
    assertEquals(Dotnewline.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** The line terminators of one character. */
  private static boolean isLineTerminator(int c) {
    switch (c) {
      case '\n':
      case 0x000B:
      case 0x000C:
      case '\r':
      case 0x0085:
      case 0x2028:
      case 0x2029:
        return true;
      default:
        return false;
    }
  }

  private static boolean isSurrogate(int c) {
    return Character.MIN_SURROGATE <= c && c <= Character.MAX_SURROGATE;
  }

  /** The rule that has to match the code point {@code c}. */
  private static int expectedRule(int c) {
    if (isLineTerminator(c)) {
      return NEWLINE;
    }
    return isSurrogate(c) ? SURROGATE : DOT;
  }

  private static String hex(int c) {
    return String.format("U+%04X", c);
  }

  // --- all code points

  /** Every code point as the only one in the input is one match. */
  @Test
  public void everyCodePointAlone() throws Exception {
    scan("");
    for (int cp = 0; cp <= Character.MAX_CODE_POINT; cp++) {
      final int c = cp;
      boolean isHighSurrogate =
          Character.MIN_HIGH_SURROGATE <= c && c <= Character.MAX_HIGH_SURROGATE;
      String input = new String(Character.toChars(c));
      scanner.yyreset(new StringReader(isHighSurrogate ? input + "a" : input));
      assertEquals(expectedRule(c), scanner.yylex(), () -> "rule for " + hex(c));
      assertEquals(input, scanner.yytext(), () -> "text for " + hex(c));
      assertEquals(Character.charCount(c), scanner.yylength(), () -> "length for " + hex(c));
      if (isHighSurrogate) {
        assertEquals(DOT, scanner.yylex(), () -> "rule after " + hex(c));
      }
      assertEquals(Dotnewline.YYEOF, scanner.yylex(), () -> "end of input after " + hex(c));
    }
  }

  // --- dot

  @Test
  public void dot() throws Exception {
    // U+0084 and U+0086 are next to the line terminator U+0085, U+2027 and U+202A next to
    // U+2028 and U+2029
    scan("a \t\u0000\u0084\u0086\u2027\u202A");
    assertToken(DOT, "a", 0, 0);
    assertToken(DOT, " ", 0, 1);
    assertToken(DOT, "\t", 0, 2);
    assertToken(DOT, "\u0000", 0, 3);
    assertToken(DOT, "\u0084", 0, 4);
    assertToken(DOT, "\u0086", 0, 5);
    assertToken(DOT, "\u2027", 0, 6);
    assertToken(DOT, "\u202A", 0, 7);
    assertEof();
  }

  /** The last two characters of the BMP are not assigned ("noncharacters"), dot matches them. */
  @Test
  public void dot_endOfBmp() throws Exception {
    scan("\uFFFD\uFFFE\uFFFF");
    assertToken(DOT, "\uFFFD", 0, 0);
    assertToken(DOT, "\uFFFE", 0, 1);
    assertToken(DOT, "\uFFFF", 0, 2);
    assertEof();
  }

  /** A supplementary character is one match of dot with two chars. */
  @Test
  public void dot_supplementary() throws Exception {
    // U+10000, U+1F600 and U+10FFFF
    scan("\uD800\uDC00\uD83D\uDE00\uDBFF\uDFFF");
    assertToken(DOT, "\uD800\uDC00", 0, 0);
    assertToken(DOT, "\uD83D\uDE00", 0, 2);
    assertToken(DOT, "\uDBFF\uDFFF", 0, 4);
    assertEof();
  }

  // --- \R

  /** Each line terminator is one match of {@code \R} and increases line count. */
  @Test
  public void newline() throws Exception {
    scan("a\nb\u000Bc\u000Cd\re\u0085f\u2028g\u2029h");
    assertToken(DOT, "a", 0, 0);
    assertToken(NEWLINE, "\n", 0, 1);
    assertToken(DOT, "b", 1, 0);
    assertToken(NEWLINE, "\u000B", 1, 1);
    assertToken(DOT, "c", 2, 0);
    assertToken(NEWLINE, "\u000C", 2, 1);
    assertToken(DOT, "d", 3, 0);
    assertToken(NEWLINE, "\r", 3, 1);
    assertToken(DOT, "e", 4, 0);
    assertToken(NEWLINE, "\u0085", 4, 1);
    assertToken(DOT, "f", 5, 0);
    assertToken(NEWLINE, "\u2028", 5, 1);
    assertToken(DOT, "g", 6, 0);
    assertToken(NEWLINE, "\u2029", 6, 1);
    assertToken(DOT, "h", 7, 0);
    assertEof();
  }

  /** CR LF is one match and one line. */
  @Test
  public void newline_crLf() throws Exception {
    scan("a\r\nb");
    assertToken(DOT, "a", 0, 0);
    assertToken(NEWLINE, "\r\n", 0, 1);
    assertToken(DOT, "b", 1, 0);
    assertEof();
  }

  /** LF CR are two matches and two lines. */
  @Test
  public void newline_lfCr() throws Exception {
    scan("a\n\rb");
    assertToken(DOT, "a", 0, 0);
    assertToken(NEWLINE, "\n", 0, 1);
    assertToken(NEWLINE, "\r", 1, 0);
    assertToken(DOT, "b", 2, 0);
    assertEof();
  }

  /** In CR CR LF, the second CR is the one that belongs to LF. */
  @Test
  public void newline_crCrLf() throws Exception {
    scan("a\r\r\nb");
    assertToken(DOT, "a", 0, 0);
    assertToken(NEWLINE, "\r", 0, 1);
    assertToken(NEWLINE, "\r\n", 1, 0);
    assertToken(DOT, "b", 2, 0);
    assertEof();
  }

  /** CR LF only pairs up with LF. Another line terminator after CR is a separate match. */
  @Test
  public void newline_crOther() throws Exception {
    scan("\r\u0085\r\u2028\n\n");
    assertToken(NEWLINE, "\r", 0, 0);
    assertToken(NEWLINE, "\u0085", 1, 0);
    assertToken(NEWLINE, "\r", 2, 0);
    assertToken(NEWLINE, "\u2028", 3, 0);
    assertToken(NEWLINE, "\n", 4, 0);
    assertToken(NEWLINE, "\n", 5, 0);
    assertEof();
  }

  /** The scanner has to look for LF after CR, and finds the end of input. */
  @Test
  public void newline_crAtEndOfInput() throws Exception {
    scan("a\r");
    assertToken(DOT, "a", 0, 0);
    assertToken(NEWLINE, "\r", 0, 1);
    assertEof();
  }

  @Test
  public void newline_crLfAtEndOfInput() throws Exception {
    scan("\r\n");
    assertToken(NEWLINE, "\r\n", 0, 0);
    assertEof();
  }

  // --- unpaired surrogates

  /**
   * Dot does not match an unpaired surrogate, only the separate unpaired surrogate rule does. A low
   * surrogate before a high surrogate is not a pair.
   */
  @Test
  public void unpairedSurrogates() throws Exception {
    scan("\uDC00\uD800a\uDBFF\uD800\uDFFFb\uDC00");
    assertToken(SURROGATE, "\uDC00", 0, 0);
    assertToken(SURROGATE, "\uD800", 0, 1);
    assertToken(DOT, "a", 0, 2);
    assertToken(SURROGATE, "\uDBFF", 0, 3);
    // U+D800 U+DFFF is a pair, U+103FF
    assertToken(DOT, "\uD800\uDFFF", 0, 4);
    assertToken(DOT, "b", 0, 6);
    assertToken(SURROGATE, "\uDC00", 0, 7);
    assertEof();
  }

  /** A high surrogate before a line terminator is unpaired. */
  @Test
  public void unpairedSurrogates_beforeNewline() throws Exception {
    scan("\uD800\r\n\uD800\n");
    assertToken(SURROGATE, "\uD800", 0, 0);
    assertToken(NEWLINE, "\r\n", 0, 1);
    assertToken(SURROGATE, "\uD800", 1, 0);
    assertToken(NEWLINE, "\n", 1, 1);
    assertEof();
  }

  /** An unpaired high surrogate as the last char of the input is like one anywhere else. */
  @Disabled(
      "JFlex bug: when the input ends with a high surrogate, zzRefill() reports the end of input"
          + " although it has read characters. yylex() returns YYEOF even though there are more"
          + " tokens, the next call to yylex() returns those tokens.")
  @Test
  public void highSurrogateAtEndOfInput() throws Exception {
    scan("a\uD800");
    assertToken(DOT, "a", 0, 0);
    assertToken(SURROGATE, "\uD800", 0, 1);
    assertEof();
    scan("\uDBFF");
    assertToken(SURROGATE, "\uDBFF", 0, 0);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
