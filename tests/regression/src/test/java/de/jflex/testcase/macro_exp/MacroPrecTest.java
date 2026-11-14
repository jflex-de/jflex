/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/** Tests that macros are expressions not text replacement. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/macro_exp/macro_prec.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/macro_exp/macro_prec.sysout",
    quiet = true,
    minimizedDfaStatesCount = 17,
    distinctCharClasses = {"abcdeghpquvxy<>[f"},
    sameCharClass = {"f\n\u0000z\uFFFF😀"})
public class MacroPrecTest implements JFlexTest {

  /** <code>{A}c</code> with {@code A = a|b} */
  private static final int CONCAT = 1;

  /** <code>{D}+</code> with {@code D = d|e} */
  private static final int PLUS = 2;

  /** <code>{G}{2}</code> with {@code G = g|h} */
  private static final int REPEAT = 3;

  /** <code>"&lt;" !{P} "&gt;"</code> with {@code P = p|q} */
  private static final int NOT = 4;

  /** <code>"[" ~{U}</code> with {@code U = u|v} */
  private static final int UPTO = 5;

  /** <code>{S}+</code> with {@code S = xy} */
  private static final int SEQ = 6;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private MacroPrec scanner;

  private void scan(String input) {
    scanner = new MacroPrec(new StringReader(input));
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
    assertEquals(MacroPrec.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  private void assertTokens(int rule, String... texts) throws IOException {
    scan(String.join("", texts));
    int column = 0;
    for (String text : texts) {
      assertToken(rule, text, 0, column);
      column += text.length();
    }
    assertEof();
  }

  /** Checks that the scanner matches every character of the one-line input with {@code [^]}. */
  private void assertOnlyFallback(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1), 0, i);
    }
    assertEof();
  }

  /** <code>{A}c</code> is {@code (a|b)c}, not {@code a|bc}. */
  @Test
  public void concatenation() throws Exception {
    assertTokens(CONCAT, "ac", "bc");
    assertOnlyFallback("a");
    assertOnlyFallback("b");
    assertOnlyFallback("c");
    scan("abc");
    assertToken(OTHER, "a", 0, 0);
    assertToken(CONCAT, "bc", 0, 1);
    assertEof();
  }

  /** <code>{D}+</code> is {@code (d|e)+}, not {@code d|e+}. */
  @Test
  public void iteration() throws Exception {
    assertTokens(PLUS, "d");
    assertTokens(PLUS, "e");
    assertTokens(PLUS, "deed");
    assertTokens(PLUS, "ddee");
  }

  /** <code>{G}{2}</code> is {@code (g|h){2}}, not {@code g|h{2}}. */
  @Test
  public void repeat() throws Exception {
    assertTokens(REPEAT, "gg", "gh", "hg", "hh");
    assertOnlyFallback("g");
    assertOnlyFallback("h");
    scan("ghg");
    assertToken(REPEAT, "gh", 0, 0);
    assertToken(OTHER, "g", 0, 2);
    assertEof();
  }

  @Test
  public void negation() throws Exception {
    assertOnlyFallback("<p>");
    assertOnlyFallback("<q>");
    assertTokens(NOT, "<>");
    assertTokens(NOT, "<pq>");
    assertTokens(NOT, "<qp>");
    assertTokens(NOT, "<pp>");
    assertTokens(NOT, "<x>");
    // the negation is "p>", the rule has no other end than the last ">"
    assertTokens(NOT, "<p>>");
  }

  @Test
  public void upto() throws Exception {
    assertTokens(UPTO, "[u");
    assertTokens(UPTO, "[v");
    scan("[xxvyyu");
    assertToken(UPTO, "[xxv", 0, 0);
    assertToken(OTHER, "y", 0, 4);
    assertToken(OTHER, "y", 0, 5);
    assertToken(OTHER, "u", 0, 6);
    assertEof();
    scan("[yyuv");
    assertToken(UPTO, "[yyu", 0, 0);
    assertToken(OTHER, "v", 0, 4);
    assertEof();
    assertOnlyFallback("[yy");
  }

  /** <code>{S}+</code> is {@code (xy)+}, not {@code xy+}. */
  @Test
  public void iterationOfConcatenation() throws Exception {
    assertTokens(SEQ, "xy");
    assertTokens(SEQ, "xyxyxy");
    scan("xyyxxy");
    assertToken(SEQ, "xy", 0, 0);
    assertToken(OTHER, "y", 0, 2);
    assertToken(OTHER, "x", 0, 3);
    assertToken(SEQ, "xy", 0, 4);
    assertEof();
    assertOnlyFallback("x");
    assertOnlyFallback("yx");
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
