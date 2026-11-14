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

/**
 * Test for bug <a href="https://github.com/jflex-de/jflex/issues/57">#57 [Macro error on regexp
 * negation (!)]</a>: wrong warnings for negated macros.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/macro_exp/macro.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/macro_exp/macro.sysout",
    quiet = true,
    minimizedDfaStatesCount = 11,
    minimizedDfa = {
      "states 11, inputs 6, lexical states 1",
      "entry: 0 0",
      "0: 0-1 -> 1; 2-3 -> 2; 4 -> 3; 5 -> 4",
      "1 [a0]:",
      "2 [a1]: 2-3 -> 2; 4 -> 5; 5 -> 6",
      "3 [a1]: 2-3 -> 2; 4 -> 7; 5 -> 6",
      "4 [a1]: 2 -> 2; 3 -> 8; 4 -> 5; 5 -> 6",
      "5: 2-3 -> 2; 5 -> 6",
      "6: 2 -> 2; 4 -> 5; 5 -> 6",
      "7 [a2]: 0 -> 7; 2-5 -> 7",
      "8: 0-2 -> 8; 3 -> 9; 4-5 -> 8",
      "9: 0-2 -> 8; 3 -> 9; 4 -> 8; 5 -> 10",
      "10 [a2]:",
      "a0: return 0;",
      "a1: return 2;",
      "a2: return 1;"
    },
    charClassesCount = 6,
    charClasses = {
      "{ [0-9][11-' ']['\"']['\''-')'][',']['.']['0'-';']['A'-']']['_']['a'-'{']['}'][127-1114111]"
          + " }",
      "{ [10] }",
      "{ ['!']['#'-'&']['+']['<'-'@']['^']['`']['|']['~'] }",
      "{ ['*'] }",
      "{ ['-'] }",
      "{ ['/'] }"
    })
public class MacroTest implements JFlexTest {

  /** {@code ("/*"~"*\/")|("--"[^\n]*)} */
  private static final int COMMENT = 1;

  /** {@code {operator}} */
  private static final int OPERATOR = 2;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private Macro scanner;

  private void scan(String input) {
    scanner = new Macro(new StringReader(input));
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
    assertEquals(Macro.YYEOF, scanner.yylex());
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

  @Test
  public void operatorsBeforeComments() throws Exception {
    scan("# #/ #/* comment */ #-- comment");
    assertToken(OPERATOR, "#", 0, 0);
    assertToken(OTHER, " ", 0, 1);
    // "#/" ends in "/"
    assertToken(OPERATOR, "#", 0, 2);
    assertToken(OPERATOR, "/", 0, 3);
    assertToken(OTHER, " ", 0, 4);
    // "#/" and "#/*" are illegal, the comment begins after "#"
    assertToken(OPERATOR, "#", 0, 5);
    assertToken(COMMENT, "/* comment */", 0, 6);
    assertToken(OTHER, " ", 0, 19);
    // "#-" and "#--" are illegal
    assertToken(OPERATOR, "#", 0, 20);
    assertToken(COMMENT, "-- comment", 0, 21);
    assertEof();
  }

  // --- legal operators

  @Test
  public void severalCharacters() throws Exception {
    assertTokens(OPERATOR, "<=");
    assertTokens(OPERATOR, "+*<>=~!@#%^&|`?$");
  }

  /** {@code -} and {@code /} are legal at the begining and in the middle, also twice. */
  @Test
  public void minusAndSlashNotAtTheEnd() throws Exception {
    assertTokens(OPERATOR, "-=");
    assertTokens(OPERATOR, "/=");
    assertTokens(OPERATOR, "<-=");
    assertTokens(OPERATOR, "</=");
    assertTokens(OPERATOR, "-/-/=");
    assertTokens(OPERATOR, "//=");
    assertTokens(OPERATOR, "-+-+");
  }

  @Test
  public void minusOrSlashAlone() throws Exception {
    assertTokens(OPERATOR, "-");
    assertTokens(OPERATOR, "/");
  }

  @Test
  public void otherCharactersSeparateOperators() throws Exception {
    scan("a<=b\n>=");
    assertToken(OTHER, "a", 0, 0);
    assertToken(OPERATOR, "<=", 0, 1);
    assertToken(OTHER, "b", 0, 3);
    assertToken(OTHER, "\n", 0, 4);
    assertToken(OPERATOR, ">=", 1, 0);
    assertEof();
  }

  // --- illegalOperator, branch {operatorChar}+[\-\/]

  /** The scanner goes back to the longest legal operator, here by one character. */
  @Test
  public void endsInMinusOrSlash() throws Exception {
    assertTokens(OPERATOR, "+", "-");
    assertTokens(OPERATOR, "<", "/");
    assertTokens(OPERATOR, "*", "/");
    assertTokens(OPERATOR, "<=", "-");
    assertTokens(OPERATOR, "<-=", "/");
  }

  /** No prefix with two or more characters is legal, the scanner goes back to the first. */
  @Test
  public void everyPrefixEndsInMinusOrSlash() throws Exception {
    assertTokens(OPERATOR, "<", "-", "/");
    assertTokens(OPERATOR, "-", "/", "-", "/");
    assertTokens(OPERATOR, "/", "/", "/");
    assertTokens(OPERATOR, "/", "-");
  }

  // --- illegalOperator, branch {operatorChar}*("--"|"/*"){operatorChar}*

  /** {@code <--=} does not end in {@code -}, but it contains the start of a comment. */
  @Test
  public void containsLineComment() throws Exception {
    scan("<--=");
    assertToken(OPERATOR, "<", 0, 0);
    assertToken(COMMENT, "--=", 0, 1);
    assertEof();
  }

  @Test
  public void containsBlockComment() throws Exception {
    scan("</*=*/>=");
    assertToken(OPERATOR, "<", 0, 0);
    assertToken(COMMENT, "/*=*/", 0, 1);
    assertToken(OPERATOR, ">=", 0, 6);
    assertEof();
  }

  /**
   * Without the end of the comment there is no comment, but operators still must not contain {@code
   * /*}.
   */
  @Test
  public void containsStartOfBlockCommentWithoutEnd() throws Exception {
    assertTokens(OPERATOR, "<", "/", "*=");
    assertTokens(OPERATOR, "*", "/", "*");
    assertTokens(OPERATOR, "/", "*");
  }

  @Test
  public void blockCommentWithoutEndAtEndOfInput() throws Exception {
    scan("/* a");
    assertToken(OPERATOR, "/", 0, 0);
    assertToken(OPERATOR, "*", 0, 1);
    assertToken(OTHER, " ", 0, 2);
    assertToken(OTHER, "a", 0, 3);
    assertEof();
  }

  // --- comments

  @Test
  public void lineComment() throws Exception {
    assertTokens(COMMENT, "--");
    assertTokens(COMMENT, "---");
    assertTokens(COMMENT, "--x");
    assertTokens(COMMENT, "-- /* <= */ x");
  }

  /** The line end is not part of a line comment. */
  @Test
  public void lineCommentEndsBeforeLineFeed() throws Exception {
    scan("--x\n-\n--\n");
    assertToken(COMMENT, "--x", 0, 0);
    assertToken(OTHER, "\n", 0, 3);
    assertToken(OPERATOR, "-", 1, 0);
    assertToken(OTHER, "\n", 1, 1);
    assertToken(COMMENT, "--", 2, 0);
    assertToken(OTHER, "\n", 2, 2);
    assertEof();
  }

  @Test
  public void blockComment() throws Exception {
    assertTokens(COMMENT, "/**/");
    assertTokens(COMMENT, "/***/");
    assertTokens(COMMENT, "/*/*/");
    assertTokens(COMMENT, "/* -- */");
  }

  /** The {@code *} of {@code /*} is not the {@code *} of the end. */
  @Test
  public void slashStarSlashIsNoComment() throws Exception {
    assertTokens(OPERATOR, "/", "*", "/");
  }

  @Test
  public void blockCommentBetweenOtherCharacters() throws Exception {
    scan("a/*b*/c");
    assertToken(OTHER, "a", 0, 0);
    assertToken(COMMENT, "/*b*/", 0, 1);
    assertToken(OTHER, "c", 0, 6);
    assertEof();
  }

  /** {@code ~} stops at the first end of comment. */
  @Test
  public void blockCommentEndsAtFirstEnd() throws Exception {
    scan("/*a*/b*/");
    assertToken(COMMENT, "/*a*/", 0, 0);
    assertToken(OTHER, "b", 0, 5);
    assertToken(OPERATOR, "*", 0, 6);
    assertToken(OPERATOR, "/", 0, 7);
    assertEof();
  }

  @Test
  public void blockCommentOverSeveralLines() throws Exception {
    scan("/* a\n-- b */x");
    assertToken(COMMENT, "/* a\n-- b */", 0, 0);
    assertToken(OTHER, "x", 1, 7);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
