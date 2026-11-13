/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.fixed_look;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for lookahead expressions {@code r1 / r2} where {@code r1} or {@code r2} has a fixed length.
 *
 * <p>The generator distinguishes two cases. If the base {@code r1} has a fixed length, the match
 * ends that many characters after its start (rules {@code "bb"/"b"+} and {@code "ab"/"cde"+}).
 * Otherwise, if the lookahead {@code r2} has a fixed length, the match ends that many characters
 * before the end of base and lookahead (rules {@code "ab"+/"cde"}, {@code "abcd"+/"f"} and {@code
 * "a"+/"aa"}).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/fixed_look/fixedlook.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/fixed_look/fixedlook.sysout",
    quiet = true,
    minimizedDfaStatesCount = 25,
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
public class FixedlookTest implements JFlexTest {

  /** {@code "ab"+/"cde"}, fixed lookahead length */
  private static final int AB_REP = 2;

  /** {@code "abcd"+/"f"}, fixed lookahead length */
  private static final int ABCD_REP = 7;

  /** {@code "a"+/"aa"}, fixed lookahead length */
  private static final int A_REP = 1;

  /** {@code "bb"/"b"+}, fixed base length */
  private static final int BB = 3;

  /** {@code "ab"/"cde"+}, fixed base length */
  private static final int AB = 4;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private Fixedlook scanner;

  private void scan(String input) {
    scanner = new Fixedlook(new StringReader(input));
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
    assertEquals(Fixedlook.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Checks that the scanner matches every character of the input with the fallback rule. */
  private void assertNoLookaheadMatch(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1), 0, i);
    }
    assertEof();
  }

  /** The lookahead is not part of the text and is scanned again. */
  @Test
  public void lookaheadIsScannedAgain() throws Exception {
    scan("abcde\n");
    assertToken(AB_REP, "ab", 0, 0);
    assertToken(OTHER, "c", 0, 2);
    assertToken(OTHER, "d", 0, 3);
    assertToken(OTHER, "e", 0, 4);
    assertToken(OTHER, "\n", 0, 5);
    assertEof();
  }

  /** A base without its lookahead is no match: the first {@code ab} is not followed by "cde". */
  @Test
  public void baseWithoutLookahead() throws Exception {
    scan("ababcdfabcde\n");
    assertToken(OTHER, "a", 0, 0);
    assertToken(OTHER, "b", 0, 1);
    assertToken(ABCD_REP, "abcd", 0, 2);
    assertToken(OTHER, "f", 0, 6);
    assertToken(AB_REP, "ab", 0, 7);
    assertToken(OTHER, "c", 0, 9);
    assertToken(OTHER, "d", 0, 10);
    assertToken(OTHER, "e", 0, 11);
    assertToken(OTHER, "\n", 0, 12);
    assertEof();
  }

  /** Bases of variable length before a lookahead of fixed length. */
  @Test
  public void repeatedBase() throws Exception {
    scan("ababcdfaaababcdeaaaaaa\n");
    assertToken(OTHER, "a", 0, 0);
    assertToken(OTHER, "b", 0, 1);
    assertToken(ABCD_REP, "abcd", 0, 2);
    assertToken(OTHER, "f", 0, 6);
    // "aaa": one "a" and the lookahead
    assertToken(A_REP, "a", 0, 7);
    // "aab": no lookahead left
    assertToken(OTHER, "a", 0, 8);
    assertToken(AB_REP, "abab", 0, 9);
    assertToken(OTHER, "c", 0, 13);
    assertToken(OTHER, "d", 0, 14);
    assertToken(OTHER, "e", 0, 15);
    // six "a": four and the lookahead
    assertToken(A_REP, "aaaa", 0, 16);
    assertToken(OTHER, "a", 0, 20);
    assertToken(OTHER, "a", 0, 21);
    assertToken(OTHER, "\n", 0, 22);
    assertEof();
  }

  /**
   * For {@code abcdecde}, the later rule {@code "ab"/"cde"+} wins over {@code "ab"+/"cde"}: the
   * length that decides which rule matches includes the lookahead, here 8 against 5, although the
   * text is {@code ab} for both. For {@code abcde} both have length 5 and the earlier rule wins.
   */
  @Test
  public void longestMatchIncludesLookahead() throws Exception {
    scan("abbbabcdfaaaabcdecdeabcde\n");
    assertToken(OTHER, "a", 0, 0);
    assertToken(BB, "bb", 0, 1);
    assertToken(OTHER, "b", 0, 3);
    assertToken(ABCD_REP, "abcd", 0, 4);
    assertToken(OTHER, "f", 0, 8);
    assertToken(A_REP, "aa", 0, 9);
    assertToken(OTHER, "a", 0, 11);
    assertToken(AB, "ab", 0, 12);
    assertToken(OTHER, "c", 0, 14);
    assertToken(OTHER, "d", 0, 15);
    assertToken(OTHER, "e", 0, 16);
    assertToken(OTHER, "c", 0, 17);
    assertToken(OTHER, "d", 0, 18);
    assertToken(OTHER, "e", 0, 19);
    assertToken(AB_REP, "ab", 0, 20);
    assertToken(OTHER, "c", 0, 22);
    assertToken(OTHER, "d", 0, 23);
    assertToken(OTHER, "e", 0, 24);
    assertToken(OTHER, "\n", 0, 25);
    assertEof();
  }

  @Test
  public void repeatedBaseBeforeLongerLookahead() throws Exception {
    scan("abcdabcdf");
    assertToken(ABCD_REP, "abcdabcd", 0, 0);
    assertToken(OTHER, "f", 0, 8);
    assertEof();
  }

  /** The text is the base of fixed length, however long the lookahead is. */
  @Test
  public void repeatedLookahead() throws Exception {
    scan("abcdecdecde");
    assertToken(AB, "ab", 0, 0);
    for (int i = 2; i < 11; i++) {
      assertToken(OTHER, "cdecdecde".substring(i - 2, i - 1), 0, i);
    }
    assertEof();
  }

  /** {@code "bb"/"b"+}: the lookahead of one match cannot be the base of the next. */
  @Test
  public void repeatedLookaheadLeavesNoBase() throws Exception {
    scan("bbbb");
    assertToken(BB, "bb", 0, 0);
    assertToken(OTHER, "b", 0, 2);
    assertToken(OTHER, "b", 0, 3);
    assertEof();
  }

  @Test
  public void secondLine() throws Exception {
    scan("b\nbbb\naaa");
    assertToken(OTHER, "b", 0, 0);
    assertToken(OTHER, "\n", 0, 1);
    assertToken(BB, "bb", 1, 0);
    assertToken(OTHER, "b", 1, 2);
    assertToken(OTHER, "\n", 1, 3);
    assertToken(A_REP, "a", 2, 0);
    assertToken(OTHER, "a", 2, 1);
    assertToken(OTHER, "a", 2, 2);
    assertEof();
  }

  /** At the end of input, a base without lookahead or with an incomplete one is no match. */
  @Test
  public void lookaheadCutOff() throws Exception {
    assertNoLookaheadMatch("ab");
    assertNoLookaheadMatch("abc");
    assertNoLookaheadMatch("abcd");
    assertNoLookaheadMatch("abcdcd");
    assertNoLookaheadMatch("bb");
    assertNoLookaheadMatch("a");
  }

  /** {@code "a"+/"aa"}: two {@code a} are only the lookahead. */
  @Test
  public void twoA() throws Exception {
    assertNoLookaheadMatch("aa");
  }

  /**
   * {@code "a"+/"aa"}: the last two {@code a} are the lookahead, and they are not matched again.
   */
  @Test
  public void threeA() throws Exception {
    scan("aaa");
    assertToken(A_REP, "a", 0, 0);
    assertToken(OTHER, "a", 0, 1);
    assertToken(OTHER, "a", 0, 2);
    assertEof();
  }

  @Test
  public void fourA() throws Exception {
    scan("aaaa");
    assertToken(A_REP, "aa", 0, 0);
    assertToken(OTHER, "a", 0, 2);
    assertToken(OTHER, "a", 0, 3);
    assertEof();
  }

  /**
   * {@code yypushback(1)} in the action of a rule with a lookahead of fixed length gives back the
   * last character of the base, not of the lookahead.
   */
  @Test
  public void pushbackWithFixedLookahead() throws Exception {
    scan("ababcde");
    scanner.pushback = true;
    assertToken(AB_REP, "aba", 0, 0);
    assertToken(OTHER, "b", 0, 3);
    assertToken(OTHER, "c", 0, 4);
    assertToken(OTHER, "d", 0, 5);
    assertToken(OTHER, "e", 0, 6);
    assertEof();
  }

  /** The same for a base of fixed length. */
  @Test
  public void pushbackWithFixedBase() throws Exception {
    scan("abcdecde");
    scanner.pushback = true;
    assertToken(AB, "a", 0, 0);
    assertToken(OTHER, "b", 0, 1);
    assertToken(OTHER, "c", 0, 2);
    assertToken(OTHER, "d", 0, 3);
    assertToken(OTHER, "e", 0, 4);
    assertToken(OTHER, "c", 0, 5);
    assertToken(OTHER, "d", 0, 6);
    assertToken(OTHER, "e", 0, 7);
    assertEof();
  }

  /** The character that was pushed back can start the next match. */
  @Test
  public void pushbackIsScannedAgain() throws Exception {
    scan("bbbb");
    scanner.pushback = true;
    assertToken(BB, "b", 0, 0);
    // "bbb" is left
    assertToken(BB, "b", 0, 1);
    assertToken(OTHER, "b", 0, 2);
    assertToken(OTHER, "b", 0, 3);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
