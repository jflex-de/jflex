/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_in_macro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/1089">#1089 (lookahead in macros)</a>,
 * reported in <a href="https://github.com/jflex-de/jflex/issues/1088">#1088</a>: a macro can define
 * an expression with lookahead ({@code /} and {@code $}).
 *
 * <p>{@code LookInMacroRef.flex} has the same rules without lookahead macros and is used as the
 * reference.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/look_in_macro/LookInMacro.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 41)
public class LookInMacroTest implements JFlexTest {

  /** <code>{stopped}</code>, lookahead of fixed length */
  private static final int STOPPED = 1;

  /** <code>({fixedBase})</code>, base of fixed length */
  private static final int FIXED_BASE = 2;

  /** <code>{choice}</code>, finite choice lookahead */
  private static final int CHOICE = 3;

  /** <code>{viaMacro}</code>, general lookahead */
  private static final int GENERAL = 4;

  /** <code>{eol}</code> */
  private static final int EOL = 5;

  /** <code>( ({lookEol}) )</code> */
  private static final int LOOK_EOL = 6;

  /** {@code \R} */
  private static final int NEWLINE = 7;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private LookInMacro scanner;

  private void scan(String input) {
    scanner = new LookInMacro(new StringReader(input));
  }

  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.yyline(), "line " + at);
    assertEquals(column, scanner.yycolumn(), "column " + at);
  }

  private void assertOther(String text, int line, int column) throws IOException {
    for (int i = 0; i < text.length(); i++) {
      assertToken(OTHER, text.substring(i, i + 1), line, column + i);
    }
  }

  private void assertEof() throws IOException {
    assertEquals(LookInMacro.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** The example of issue #1088. */
  @Test
  public void stopped() throws Exception {
    scan("somethingstop");
    assertToken(STOPPED, "something", 0, 0);
    assertOther("stop", 0, 9);
    assertEof();
  }

  /** Without the lookahead there is no match. */
  @Test
  public void notStopped() throws Exception {
    scan("something sto");
    assertOther("something sto", 0, 0);
    assertEof();
  }

  /** The base is the longest match. */
  @Test
  public void stoppedTwice() throws Exception {
    scan("astopstop");
    assertToken(STOPPED, "astop", 0, 0);
    assertOther("stop", 0, 5);
    assertEof();
  }

  @Test
  public void fixedBase() throws Exception {
    scan("ab12 ab ab7");
    assertToken(FIXED_BASE, "ab", 0, 0);
    assertOther("12 ab ", 0, 2);
    assertToken(FIXED_BASE, "ab", 0, 8);
    assertOther("7", 0, 10);
    assertEof();
  }

  @Test
  public void choice() throws Exception {
    scan("12x 345yy 6y 7");
    assertToken(CHOICE, "12", 0, 0);
    assertOther("x ", 0, 2);
    assertToken(CHOICE, "345", 0, 4);
    assertOther("yy 6y 7", 0, 7);
    assertEof();
  }

  /** The base {@code [A-Z]+} is the longest match and leaves an empty {@code [A-Z]*}. */
  @Test
  public void general() throws Exception {
    scan("ABC! AB A!");
    assertToken(GENERAL, "ABC", 0, 0);
    assertOther("! AB ", 0, 3);
    assertToken(GENERAL, "A", 0, 8);
    assertOther("!", 0, 9);
    assertEof();
  }

  /** The end of input is not an end of line for {@code $}. */
  @Test
  public void eol() throws Exception {
    scan("##\n# #\r\n#");
    assertToken(EOL, "##", 0, 0);
    assertToken(NEWLINE, "\n", 0, 2);
    assertOther("# ", 1, 0);
    assertToken(EOL, "#", 1, 2);
    assertToken(NEWLINE, "\r\n", 1, 3);
    assertOther("#", 2, 0);
    assertEof();
  }

  @Test
  public void lookEol() throws Exception {
    scan("@@;\n@; \n@\n");
    assertToken(LOOK_EOL, "@@", 0, 0);
    assertOther(";", 0, 2);
    assertToken(NEWLINE, "\n", 0, 3);
    assertOther("@; ", 1, 0);
    assertToken(NEWLINE, "\n", 1, 3);
    assertOther("@", 2, 0);
    assertToken(NEWLINE, "\n", 2, 1);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }

  // --- comparison with the no-macro spec

  /** The matches of the scanner with lookahead macros: rule, position and text. */
  private static List<String> macroTokens(String input) throws IOException {
    List<String> tokens = new ArrayList<>();
    LookInMacro s = new LookInMacro(new StringReader(input));
    int rule;
    while ((rule = s.yylex()) != LookInMacro.YYEOF) {
      tokens.add(rule + " " + s.yyline() + ":" + s.yycolumn() + " '" + s.yytext() + "'");
    }
    return tokens;
  }

  /** The matches of the reference scanner, in the format of {@link #macroTokens}. */
  private static List<String> referenceTokens(String input) throws IOException {
    List<String> tokens = new ArrayList<>();
    LookInMacroRef s = new LookInMacroRef(new StringReader(input));
    int rule;
    while ((rule = s.yylex()) != LookInMacroRef.YYEOF) {
      tokens.add(rule + " " + s.yyline() + ":" + s.yycolumn() + " '" + s.yytext() + "'");
    }
    return tokens;
  }

  /** The pieces that the inputs of {@link #sameAsLookaheadInRules} are made of. */
  private static final String[] PIECES = {
    "a", "stop", "ab", "1", "x", "yy", "A", "!", "#", "@", ";", "\n", "\r\n", " "
  };

  /** All inputs of up to four pieces give the same matches in both scanners. */
  @Test
  public void sameAsLookaheadInRules() throws Exception {
    int n = PIECES.length;
    int count = 0;
    for (int length = 1; length <= 4; length++) {
      int total = (int) Math.pow(n, length);
      for (int i = 0; i < total; i++) {
        StringBuilder input = new StringBuilder();
        for (int j = 0, rest = i; j < length; j++, rest /= n) {
          input.append(PIECES[rest % n]);
        }
        String text = input.toString();
        assertEquals(referenceTokens(text), macroTokens(text), () -> "input: " + text);
        count++;
      }
    }
    assertEquals(n + n * n + n * n * n + n * n * n * n, count);
  }
}
