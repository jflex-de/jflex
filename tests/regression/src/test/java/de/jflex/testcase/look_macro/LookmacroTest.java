/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_macro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for bug <a href="https://github.com/jflex-de/jflex/issues/100">#100 (macro use in length
 * choice lookahead)</a>. Macros are expanded before the generator decides which kind of lookahead a
 * rule has.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/look_macro/lookmacro.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/look_macro/lookmacro.sysout",
    quiet = true,
    minimizedDfaStatesCount = 21,
    charClassesCount = 15,
    sameCharClass = {
      "abzAZ\u00E9\u00DF\u65E5\u672C\uD835\uDC00",
      "01234",
      "56789",
      "\u000B\u000C\u0085\u2028\u2029",
      " _-@\u0000\u0661\uD83D\uDE00"
    },
    distinctCharClasses = {" \n\u0085\r!#05:<>afo\uD800"})
public class LookmacroTest implements JFlexTest {

  /** {@code {bar}/{foo}} */
  private static final int BAR = 1;

  /** <code>{D}+/{D2}</code> */
  private static final int DIGITS = 2;

  /** <code>{tail}$</code> */
  private static final int TAIL = 3;

  /** {@code \R} */
  private static final int NEWLINE = 4;

  /** {@code (.)} */
  private static final int OTHER = 0;

  private Lookmacro scanner;

  private void scan(String input) {
    scanner = new Lookmacro(new StringReader(input));
  }

  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Lookmacro.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Checks that the scanner matches every character of the one-line input with {@code (.)}. */
  private void assertOnlyFallback(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1), 0, i);
    }
    assertEof();
  }

  // --- {bar}/{foo}

  /** The second alternative of {@code foo}. */
  @Test
  public void lettersBeforeColon() throws Exception {
    scan("x:");
    assertToken(BAR, "x", 0, 0);
    assertToken(OTHER, ":", 0, 1);
    assertEof();
  }

  /** The first alternative of {@code foo}. All of the lookahead is scanned again. */
  @Test
  public void lettersBeforeFoo() throws Exception {
    scan("x<foo>");
    assertToken(BAR, "x", 0, 0);
    assertToken(OTHER, "<", 0, 1);
    assertToken(OTHER, "f", 0, 2);
    assertToken(OTHER, "o", 0, 3);
    assertToken(OTHER, "o", 0, 4);
    assertToken(OTHER, ">", 0, 5);
    assertEof();
  }

  /** {@code bar} is one or more letters, and the letters of the lookahead are also letters. */
  @Test
  public void severalLetters() throws Exception {
    scan("foo<foo>of:");
    assertToken(BAR, "foo", 0, 0);
    assertToken(OTHER, "<", 0, 3);
    assertToken(OTHER, "f", 0, 4);
    assertToken(OTHER, "o", 0, 5);
    assertToken(OTHER, "o", 0, 6);
    assertToken(OTHER, ">", 0, 7);
    assertToken(BAR, "of", 0, 8);
    assertToken(OTHER, ":", 0, 10);
    assertEof();
  }

  /** Just the lookahead alone is not a match. */
  @Test
  public void lookaheadWithoutBase() throws Exception {
    assertOnlyFallback(":");
    assertOnlyFallback("<foo>");
    assertOnlyFallback("_:");
    assertOnlyFallback(" <foo>");
  }

  /** The lookahead is cut off by the end of input or is not complete. */
  @Test
  public void incompleteLookahead() throws Exception {
    assertOnlyFallback("x<foo");
    assertOnlyFallback("x<fo>");
    assertOnlyFallback("x<");
    assertOnlyFallback("x");
    assertOnlyFallback("xy");
    assertOnlyFallback("x <foo>");
    assertOnlyFallback("x;");
  }

  /** {@code [:letter:]} has more than the ASCII letters: Latin with accents, and CJK. */
  @Test
  public void nonAsciiLetters() throws Exception {
    scan("\u00E9t\u00E9: \u65E5\u672C<foo>");
    assertToken(BAR, "\u00E9t\u00E9", 0, 0);
    assertToken(OTHER, ":", 0, 3);
    assertToken(OTHER, " ", 0, 4);
    assertToken(BAR, "\u65E5\u672C", 0, 5);
    assertToken(OTHER, "<", 0, 7);
    assertToken(OTHER, "f", 0, 8);
    assertToken(OTHER, "o", 0, 9);
    assertToken(OTHER, "o", 0, 10);
    assertToken(OTHER, ">", 0, 11);
    assertEof();
  }

  /**
   * A letter outside the BMP (U+1D400, two {@code char}) and a character outside the BMP that is no
   * letter (U+1F600). The column counts the first as one character.
   */
  @Test
  public void supplementaryLetter() throws Exception {
    scan("\uD835\uDC00:\uD83D\uDE00:");
    assertToken(BAR, "\uD835\uDC00", 0, 0);
    assertToken(OTHER, ":", 0, 2);
    assertToken(OTHER, "\uD83D\uDE00", 0, 3);
    assertToken(OTHER, ":", 0, 5);
    assertEof();
  }

  /** Digits and other characters that are no letters. */
  @Test
  public void noLetters() throws Exception {
    assertOnlyFallback("7:");
    // ARABIC-INDIC DIGIT ONE, neither a letter nor in [0-9]
    assertOnlyFallback("\u0661:");
    assertOnlyFallback("-<foo>");
  }

  /** The input of the former golden file test. */
  @Test
  public void bothAlternatives() throws Exception {
    scan("blah<foo> blah x:d");
    assertToken(BAR, "blah", 0, 0);
    assertToken(OTHER, "<", 0, 4);
    assertToken(OTHER, "f", 0, 5);
    assertToken(OTHER, "o", 0, 6);
    assertToken(OTHER, "o", 0, 7);
    assertToken(OTHER, ">", 0, 8);
    assertToken(OTHER, " ", 0, 9);
    assertToken(OTHER, "b", 0, 10);
    assertToken(OTHER, "l", 0, 11);
    assertToken(OTHER, "a", 0, 12);
    assertToken(OTHER, "h", 0, 13);
    assertToken(OTHER, " ", 0, 14);
    assertToken(BAR, "x", 0, 15);
    assertToken(OTHER, ":", 0, 16);
    assertToken(OTHER, "d", 0, 17);
    assertEof();
  }

  // --- {D}+/{D2}

  /** The last digit is the lookahead. */
  @Test
  public void digitsBeforeLowDigit() throws Exception {
    scan("123 40");
    assertToken(DIGITS, "12", 0, 0);
    assertToken(OTHER, "3", 0, 2);
    assertToken(OTHER, " ", 0, 3);
    assertToken(DIGITS, "4", 0, 4);
    assertToken(OTHER, "0", 0, 5);
    assertEof();
  }

  /** {@code D2} is {@code D} without 5 to 9. */
  @Test
  public void highDigitIsNoLookahead() throws Exception {
    assertOnlyFallback("59");
    assertOnlyFallback("15");
    assertOnlyFallback("7");
    assertOnlyFallback("3");
  }

  /** The base {@code {D}+} has all of {@code D}: 5 and 9 are in the text. */
  @Test
  public void highDigitsInBase() throws Exception {
    scan("5912");
    assertToken(DIGITS, "591", 0, 0);
    assertToken(OTHER, "2", 0, 3);
    assertEof();
  }

  /**
   * The lookahead is the last digit from 0 to 4, the digits after that are not part of the match.
   */
  @Test
  public void lowDigitBeforeHighDigits() throws Exception {
    scan("1259");
    assertToken(DIGITS, "1", 0, 0);
    assertToken(OTHER, "2", 0, 1);
    assertToken(OTHER, "5", 0, 2);
    assertToken(OTHER, "9", 0, 3);
    assertEof();
  }

  // --- {tail}$

  @Test
  public void tailAtEndOfLine() throws Exception {
    scan("#\n!!\n");
    assertToken(TAIL, "#", 0, 0);
    assertToken(NEWLINE, "\n", 0, 1);
    assertToken(TAIL, "!!", 1, 0);
    assertToken(NEWLINE, "\n", 1, 2);
    assertEof();
  }

  /** The lookahead {@code \r\n} of {@code $} is scanned again as one match of {@code \R}. */
  @Test
  public void tailBeforeCrLf() throws Exception {
    scan("#\r\n!!\r\n!!\r#");
    assertToken(TAIL, "#", 0, 0);
    assertToken(NEWLINE, "\r\n", 0, 1);
    assertToken(TAIL, "!!", 1, 0);
    assertToken(NEWLINE, "\r\n", 1, 2);
    assertToken(TAIL, "!!", 2, 0);
    assertToken(NEWLINE, "\r", 2, 2);
    assertToken(OTHER, "#", 3, 0);
    assertEof();
  }

  /** {@code $} is for both alternatives of {@code tail}. */
  @Test
  public void tailNotAtEndOfLine() throws Exception {
    assertOnlyFallback("#x");
    assertOnlyFallback("!!x");
    assertOnlyFallback("# ");
    assertOnlyFallback("#");
    assertOnlyFallback("!!");
  }

  /**
   * Only the last {@code #} or {@code !!} of a line is a match, and one {@code !} is not a match.
   */
  @Test
  public void lastOfLine() throws Exception {
    scan("##\n!!!\n#!!\n!\n");
    assertToken(OTHER, "#", 0, 0);
    assertToken(TAIL, "#", 0, 1);
    assertToken(NEWLINE, "\n", 0, 2);
    assertToken(OTHER, "!", 1, 0);
    assertToken(TAIL, "!!", 1, 1);
    assertToken(NEWLINE, "\n", 1, 3);
    assertToken(OTHER, "#", 2, 0);
    assertToken(TAIL, "!!", 2, 1);
    assertToken(NEWLINE, "\n", 2, 3);
    assertToken(OTHER, "!", 3, 0);
    assertToken(NEWLINE, "\n", 3, 1);
    assertEof();
  }

  // --- all rules

  /** The lookahead of one rule begins a match of another rule. */
  @Test
  public void allRules() throws Exception {
    scan("ab:12#\nx<foo>!!\n");
    assertToken(BAR, "ab", 0, 0);
    assertToken(OTHER, ":", 0, 2);
    assertToken(DIGITS, "1", 0, 3);
    assertToken(OTHER, "2", 0, 4);
    assertToken(TAIL, "#", 0, 5);
    assertToken(NEWLINE, "\n", 0, 6);
    assertToken(BAR, "x", 1, 0);
    assertToken(OTHER, "<", 1, 1);
    assertToken(OTHER, "f", 1, 2);
    assertToken(OTHER, "o", 1, 3);
    assertToken(OTHER, "o", 1, 4);
    assertToken(OTHER, ">", 1, 5);
    assertToken(TAIL, "!!", 1, 6);
    assertToken(NEWLINE, "\n", 1, 8);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
