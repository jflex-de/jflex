/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_pre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for the predefined character classes {@code [:jletter:]} and {@code [:jletterdigit:]}
 * (sourceforge bug #467827).
 *
 * <p>JFlex computes both classes when it generates the scanner, from {@link
 * Character#isJavaIdentifierStart(int)} and {@link Character#isJavaIdentifierPart(int)} of the JDK
 * it runs on. The tests compare the scanner with these two methods for every code point.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_pre/ccl.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/ccl_pre/ccl.sysout",
    quiet = true,
    minimizedDfaStatesCount = 5,
    minimizedDfa = {
      "states 5, inputs 6, lexical states 1",
      "entry: 0 0",
      "0: 0-1 -> 1; 3 -> 2; 4 -> 3",
      "1 [a0]:",
      "2 [a1]: 0 -> 2; 3 -> 2; 5 -> 2",
      "3 [a0]: 3 -> 2; 4 -> 4",
      "4: 3 -> 2; 4 -> 4",
      "a0: return 0;",
      "a1: return 1;"
    },
    // U+0085 has a class of its own: it is a line terminator that is identifier-ignorable, so it
    // is in [:jletterdigit:], but "." does not match it.
    charClassesCount = 6,
    sameCharClass = {
      "09\u0000\u0301",
      " !\t\u00A0\uFFFF",
      "\uD800\n\u000B\u000C\r\u2028\u2029\uDFFF",
      "aZ$_\u00E9\u03B1\uD801\uDC00"
    },
    distinctCharClasses = {"0 \na@\u0085"})
public class CclPreTest implements JFlexTest {

  /** {@code [\@]* [:jletter:] [:jletterdigit:]*} */
  private static final int IDENTIFIER = 1;

  /** {@code .} */
  private static final int FALLBACK = 0;

  /** No rule matches, the scanner throws. */
  private static final int NO_MATCH = -2;

  private Ccl scanner;

  private void scan(String input) {
    scanner = new Ccl(new StringReader(input));
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
    assertEquals(Ccl.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Checks that no rule matches at the current position. */
  private void assertNoMatch() {
    Error error = assertThrows(Error.class, () -> scanner.yylex());
    assertEquals("Error: could not match input", error.getMessage());
  }

  /** The characters that {@code .} does not match. */
  private static boolean isExcludedFromDot(int c) {
    switch (c) {
      case '\n':
      case '\u000B':
      case '\u000C':
      case '\r':
      case '\u0085':
      case '\u2028':
      case '\u2029':
        return true;
      default:
        return Character.MIN_SURROGATE <= c && c <= Character.MAX_SURROGATE;
    }
  }

  private static int expectedAtStart(int c) {
    if (Character.isJavaIdentifierStart(c)) {
      return IDENTIFIER;
    }
    return isExcludedFromDot(c) ? NO_MATCH : FALLBACK;
  }

  private static boolean isHighSurrogate(int c) {
    return Character.MIN_HIGH_SURROGATE <= c && c <= Character.MAX_HIGH_SURROGATE;
  }

  private static String hex(int c) {
    return String.format("U+%04X", c);
  }

  /**
   * Checks that the next token is the single code point {@code c}, matched by the rule that {@link
   * #expectedAtStart} gives, and that the input ends afterwards.
   */
  private void assertSingleCodePoint(int c) throws IOException {
    int expected = expectedAtStart(c);
    if (expected == NO_MATCH) {
      assertThrows(Error.class, () -> scanner.yylex(), () -> hex(c));
      return;
    }
    assertEquals(expected, scanner.yylex(), () -> "rule for " + hex(c));
    assertEquals(Character.charCount(c), scanner.yylength(), () -> "length for " + hex(c));
    assertEquals(Ccl.YYEOF, scanner.yylex(), () -> "end of input after " + hex(c));
  }

  // --- comparison with the JDK

  /** A single code point is an identifier iff it is a Java identifier start. */
  @Test
  public void jletter_isJavaIdentifierStart() throws Exception {
    scan("");
    for (int c = 0; c <= Character.MAX_CODE_POINT; c++) {
      // skip unpaired high surrogates. They are not valid code points.
      if (isHighSurrogate(c)) {
        continue;
      }
      scanner.yyreset(new StringReader(new String(Character.toChars(c))));
      assertSingleCodePoint(c);
    }
  }

  /** A code point after a letter belongs to the identifier iff it is a Java identifier part. */
  @Test
  public void jletterdigit_isJavaIdentifierPart() throws Exception {
    scan("");
    for (int cp = 0; cp <= Character.MAX_CODE_POINT; cp++) {
      final int c = cp;
      // skip unpaired high surrogates. They are not valid code points.
      if (isHighSurrogate(c)) {
        continue;
      }
      int charCount = Character.charCount(c);
      scanner.yyreset(new StringReader("a" + new String(Character.toChars(c))));
      assertEquals(IDENTIFIER, scanner.yylex(), () -> "rule for a + " + hex(c));
      if (Character.isJavaIdentifierPart(c)) {
        assertEquals(1 + charCount, scanner.yylength(), () -> "length for a + " + hex(c));
        assertEquals(Ccl.YYEOF, scanner.yylex(), () -> "end of input after a + " + hex(c));
      } else {
        assertEquals(1, scanner.yylength(), () -> "length for a + " + hex(c));
        assertSingleCodePoint(c);
      }
    }
  }

  // --- explicit cases

  /** The input of the golden file that this test replaces. */
  @Test
  public void identifiersAndFallback() throws Exception {
    scan("@Anident 1231 @@jnother ident3");
    assertToken(IDENTIFIER, "@Anident", 0, 0);
    assertToken(FALLBACK, " ", 0, 8);
    assertToken(FALLBACK, "1", 0, 9);
    assertToken(FALLBACK, "2", 0, 10);
    assertToken(FALLBACK, "3", 0, 11);
    assertToken(FALLBACK, "1", 0, 12);
    assertToken(FALLBACK, " ", 0, 13);
    assertToken(IDENTIFIER, "@@jnother", 0, 14);
    assertToken(FALLBACK, " ", 0, 23);
    assertToken(IDENTIFIER, "ident3", 0, 24);
    assertEof();
  }

  @Test
  public void dollarAndUnderscore() throws Exception {
    scan("$ _ $_ _$1 a$_");
    assertToken(IDENTIFIER, "$", 0, 0);
    assertToken(FALLBACK, " ", 0, 1);
    assertToken(IDENTIFIER, "_", 0, 2);
    assertToken(FALLBACK, " ", 0, 3);
    assertToken(IDENTIFIER, "$_", 0, 4);
    assertToken(FALLBACK, " ", 0, 6);
    assertToken(IDENTIFIER, "_$1", 0, 7);
    assertToken(FALLBACK, " ", 0, 10);
    assertToken(IDENTIFIER, "a$_", 0, 11);
    assertEof();
  }

  /** A digit is {@code [:jletterdigit:]}, but not {@code [:jletter:]}. */
  @Test
  public void digitFirst() throws Exception {
    scan("1a");
    assertToken(FALLBACK, "1", 0, 0);
    assertToken(IDENTIFIER, "a", 0, 1);
    assertEof();
  }

  @Test
  public void digitLater() throws Exception {
    scan("a1 a123b4");
    assertToken(IDENTIFIER, "a1", 0, 0);
    assertToken(FALLBACK, " ", 0, 2);
    assertToken(IDENTIFIER, "a123b4", 0, 3);
    assertEof();
  }

  @Test
  public void atAlone() throws Exception {
    scan("@");
    assertToken(FALLBACK, "@", 0, 0);
    assertEof();
  }

  /** Without a letter after them, {@code @@} are two fallback matches. */
  @Test
  public void twoAtAlone() throws Exception {
    scan("@@");
    assertToken(FALLBACK, "@", 0, 0);
    assertToken(FALLBACK, "@", 0, 1);
    assertEof();
  }

  /** The scanner reads all {@code @} and the digit, and goes back to the first {@code @}. */
  @Test
  public void atBeforeDigit() throws Exception {
    scan("@@@1x");
    assertToken(FALLBACK, "@", 0, 0);
    assertToken(FALLBACK, "@", 0, 1);
    assertToken(FALLBACK, "@", 0, 2);
    assertToken(FALLBACK, "1", 0, 3);
    assertToken(IDENTIFIER, "x", 0, 4);
    assertEof();
  }

  /** {@code @} is only allowed before the identifier, so it starts a new one. */
  @Test
  public void atInside() throws Exception {
    scan("a@b@@@c@");
    assertToken(IDENTIFIER, "a", 0, 0);
    assertToken(IDENTIFIER, "@b", 0, 1);
    assertToken(IDENTIFIER, "@@@c", 0, 3);
    assertToken(FALLBACK, "@", 0, 7);
    assertEof();
  }

  @Test
  public void nonAsciiLetters() throws Exception {
    // e acute, Greek, CJK, and U+10400 DESERET CAPITAL LETTER LONG I as a surrogate pair
    scan("\u00E9t\u00E9 @\u03B1\u03B2 \u4E2D\u6587 \uD801\uDC00x");
    assertToken(IDENTIFIER, "\u00E9t\u00E9", 0, 0);
    assertToken(FALLBACK, " ", 0, 3);
    assertToken(IDENTIFIER, "@\u03B1\u03B2", 0, 4);
    assertToken(FALLBACK, " ", 0, 7);
    assertToken(IDENTIFIER, "\u4E2D\u6587", 0, 8);
    assertToken(FALLBACK, " ", 0, 10);
    // length 3 in chars
    assertToken(IDENTIFIER, "\uD801\uDC00x", 0, 11);
    assertEof();
  }

  /** A supplementary character that is no letter is one fallback match of two chars. */
  @Test
  public void supplementaryFallback() throws Exception {
    // U+1F600 GRINNING FACE
    scan("a\uD83D\uDE00b");
    assertToken(IDENTIFIER, "a", 0, 0);
    assertToken(FALLBACK, "\uD83D\uDE00", 0, 1);
    // the column counts chars
    assertToken(IDENTIFIER, "b", 0, 3);
    assertEof();
  }

  /**
   * As in Java, an identifier-ignorable character such as U+0000 is part of an identifier, but
   * cannot start one. The scanner does not remove it from the text.
   */
  @Test
  public void identifierIgnorable() throws Exception {
    scan("a\u0000b \u0000a");
    assertToken(IDENTIFIER, "a\u0000b", 0, 0);
    assertToken(FALLBACK, " ", 0, 3);
    assertToken(FALLBACK, "\u0000", 0, 4);
    assertToken(IDENTIFIER, "a", 0, 5);
    assertEof();
  }

  /**
   * U+0085 (next line) is identifier-ignorable, so it is in {@code [:jletterdigit:]} and inside an
   * identifier, although it is a line terminator: it counts as one for {@code %line}, and {@code .}
   * does not match it.
   */
  @Test
  public void nextLineInsideIdentifier() throws Exception {
    scan("a\u0085b c\u0085");
    assertToken(IDENTIFIER, "a\u0085b", 0, 0);
    assertToken(FALLBACK, " ", 1, 1);
    assertToken(IDENTIFIER, "c\u0085", 1, 2);
    assertEof();
  }

  @Test
  public void nextLineAlone() throws Exception {
    scan("\u0085");
    assertNoMatch();
  }

  /** The spec has no rule for line terminators: {@code .} does not match them. */
  @Test
  public void lineTerminatorsAreNotMatched() throws Exception {
    for (String lineTerminator :
        new String[] {"\n", "\u000B", "\u000C", "\r", "\u2028", "\u2029"}) {
      scan("a1" + lineTerminator + "b");
      assertToken(IDENTIFIER, "a1", 0, 0);
      assertNoMatch();
    }
  }

  /** {@code .} does not match an unpaired surrogate either. */
  @Test
  public void unpairedSurrogatesAreNotMatched() throws Exception {
    for (String surrogate : new String[] {"\uD800", "\uDBFF", "\uDC00", "\uDFFF"}) {
      scan("a" + surrogate + "b");
      assertToken(IDENTIFIER, "a", 0, 0);
      assertNoMatch();
      // a low surrogate before a high one is not a pair
      scan("\uDC00" + surrogate + "b");
      assertNoMatch();
    }
    for (String lowSurrogate : new String[] {"\uDC00", "\uDFFF"}) {
      scan("a" + lowSurrogate);
      assertToken(IDENTIFIER, "a", 0, 0);
      assertNoMatch();
    }
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
