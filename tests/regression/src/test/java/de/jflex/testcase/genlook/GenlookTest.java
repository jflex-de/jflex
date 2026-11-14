/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.genlook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for general lookahead expressions {@code r1 / r2}, where r2 is a general lookahed (neither
 * {@code r1} nor {@code r2} is fixed length and {@code r2} is not a finite choice of fixed lengths,
 * e.g. {@code a+a/a+}).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/genlook/genlook.flex",
    // the empty-match warning for "c"?
    sysout = "src/test/java/de/jflex/testcase/genlook/genlook.sysout",
    quiet = true,
    minimizedDfaStatesCount = 16,
    charClassesCount = 4,
    charClasses = {"{ [0-'`']['d'-1114111] }", "{ ['a'] }", "{ ['b'] }", "{ ['c'] }"})
public class GenlookTest implements JFlexTest {

  /** {@code "aa"|"a"/"a"+} */
  private static final int A = 1;

  /** {@code "bb"|"b"/"b"*}, the lookahead may be empty */
  private static final int B = 2;

  /** {@code "c"} */
  private static final int C = 3;

  /** {@code [^]} */
  private static final int OTHER = 0;

  // The rules "c"? (4) and [^] in END (5) cannot fire

  private Genlook scanner;

  private void scan(String input) {
    scanner = new Genlook(new StringReader(input));
  }

  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
    assertEquals(Genlook.YYINITIAL, scanner.yystate(), "lexical state " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Genlook.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
    assertEquals(Genlook.YYINITIAL, scanner.yystate(), "lexical state at EOF");
  }

  // --- "aa"|"a" / "a"+

  /** One {@code a} has no lookahead. */
  @Test
  public void oneA() throws Exception {
    scan("a");
    assertToken(OTHER, "a", 0, 0);
    assertEof();
  }

  /** Only the short alternative of the base leaves an {@code a} for the lookahead. */
  @Test
  public void twoA() throws Exception {
    scan("aa");
    assertToken(A, "a", 0, 0);
    assertToken(OTHER, "a", 0, 1);
    assertEof();
  }

  /**
   * Both {@code a / aa} and {@code aa / a} match all of the input. The text is the longest base.
   */
  @Test
  public void threeA() throws Exception {
    scan("aaa");
    assertToken(A, "aa", 0, 0);
    assertToken(OTHER, "a", 0, 2);
    assertEof();
  }

  /** The lookahead is scanned again and starts the next match of the same rule. */
  @Test
  public void fourA() throws Exception {
    scan("aaaa");
    assertToken(A, "aa", 0, 0);
    assertToken(A, "a", 0, 2);
    assertToken(OTHER, "a", 0, 3);
    assertEof();
  }

  @Test
  public void fiveA() throws Exception {
    scan("aaaaa");
    assertToken(A, "aa", 0, 0);
    assertToken(A, "aa", 0, 2);
    assertToken(OTHER, "a", 0, 4);
    assertEof();
  }

  /** The lookahead ends at another character and at a line end. */
  @Test
  public void lookaheadBeforeOtherCharacters() throws Exception {
    scan("aaab\naac");
    assertToken(A, "aa", 0, 0);
    assertToken(OTHER, "a", 0, 2);
    assertToken(B, "b", 0, 3);
    assertToken(OTHER, "\n", 0, 4);
    assertToken(A, "a", 1, 0);
    assertToken(OTHER, "a", 1, 1);
    assertToken(C, "c", 1, 2);
    assertEof();
  }

  // --- "bb"|"b" / "b"*

  /** The lookahead is empty at the end of input. */
  @Test
  public void oneBAtEof() throws Exception {
    scan("b");
    assertToken(B, "b", 0, 0);
    assertEof();
  }

  /** With an empty lookahead, the long alternative of the base matches. */
  @Test
  public void twoBAtEof() throws Exception {
    scan("bb");
    assertToken(B, "bb", 0, 0);
    assertEof();
  }

  /** The base is {@code bb} with lookahead {@code b}, which is then a later match. */
  @Test
  public void threeB() throws Exception {
    scan("bbb");
    assertToken(B, "bb", 0, 0);
    assertToken(B, "b", 0, 2);
    assertEof();
  }

  @Test
  public void fiveB() throws Exception {
    scan("bbbbb");
    assertToken(B, "bb", 0, 0);
    assertToken(B, "bb", 0, 2);
    assertToken(B, "b", 0, 4);
    assertEof();
  }

  /** The lookahead is empty before another character. */
  @Test
  public void emptyLookaheadBeforeOtherCharacters() throws Exception {
    scan("bxbba\nbbbc");
    assertToken(B, "b", 0, 0);
    assertToken(OTHER, "x", 0, 1);
    assertToken(B, "bb", 0, 2);
    assertToken(OTHER, "a", 0, 4);
    assertToken(OTHER, "\n", 0, 5);
    assertToken(B, "bb", 1, 0);
    assertToken(B, "b", 1, 2);
    assertToken(C, "c", 1, 3);
    assertEof();
  }

  // --- "c", "c"? and the lexical state END

  /** Every {@code c} is matched by {@code "c"}, not by {@code "c"?} which has lower precedence. */
  @Test
  public void ruleC() throws Exception {
    scan("cxcc");
    assertToken(C, "c", 0, 0);
    assertToken(OTHER, "x", 0, 1);
    assertToken(C, "c", 0, 2);
    assertToken(C, "c", 0, 3);
    assertEof();
  }

  /** At the end of input the scanner returns EOF, not the empty match of {@code "c"?}. */
  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
    // and stays there
    assertEof();
  }

  /** The input of the former golden file test. */
  @Test
  public void allRules() throws Exception {
    scan("xzyaaaadfabcde\n");
    assertToken(OTHER, "x", 0, 0);
    assertToken(OTHER, "z", 0, 1);
    assertToken(OTHER, "y", 0, 2);
    assertToken(A, "aa", 0, 3);
    assertToken(A, "a", 0, 5);
    assertToken(OTHER, "a", 0, 6);
    assertToken(OTHER, "d", 0, 7);
    assertToken(OTHER, "f", 0, 8);
    assertToken(OTHER, "a", 0, 9);
    assertToken(B, "b", 0, 10);
    assertToken(C, "c", 0, 11);
    assertToken(OTHER, "d", 0, 12);
    assertToken(OTHER, "e", 0, 13);
    assertToken(OTHER, "\n", 0, 14);
    assertEof();
  }
}
