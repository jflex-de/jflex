/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.empty_match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Matching the empty string. May lead to non-termination if not used carefully.
 *
 * <p>The only rule of the lexical state {@code A} is {@code ""}. Its action switches to the state
 * {@code B}, so that the scanner makes progress with the next match. Check that empty-match warning
 * is produced.
 *
 * <p>At the end of input, EOF should take precedence over the empty match.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/empty_match/emptymatch.flex",
    // the empty match warning for ""
    sysout = "src/test/java/de/jflex/testcase/empty_match/emptymatch.sysout",
    quiet = true,
    minimizedDfaStatesCount = 9,
    minimizedDfa = {
      "states 9, inputs 4, lexical states 3",
      "entry: 0 0 1 1 2 2",
      "0: 0 -> 3; 1 -> 4; 2-3 -> 3",
      "1 [a0]:",
      "2: 0-2 -> 5; 3 -> 6",
      "3 [a1]:",
      "4 [a1]: 1 -> 7; 2 -> 8",
      "5 [a2]:",
      "6 [a3]:",
      "7: 1 -> 7; 2 -> 8",
      "8 [a4]:",
      "a0: yybegin(B); return 3;",
      "a1: return 2;",
      "a2: return 5;",
      "a3: yybegin(YYINITIAL); return 4;",
      "a4: yybegin(A); return 1;"
    },
    charClassesCount = 4,
    charClasses = {
      "{ [0-'@']['\\']['^'-'`']['{'-1114111] }",
      "{ ['A'-'Z']['a'-'z'] }",
      "{ ['['] }",
      "{ [']'] }"
    })
public class EmptymatchTest implements JFlexTest {

  /** {@code [a-zA-Z]+ "["} in {@code YYINITIAL}, switches to {@code A} */
  private static final int OPEN = 1;

  /** {@code [^]} in {@code YYINITIAL} */
  private static final int OTHER = 2;

  /** {@code ""} in {@code A}, switches to {@code B} */
  private static final int EMPTY = 3;

  /** {@code "]"} in {@code B}, switches to {@code YYINITIAL} */
  private static final int CLOSE = 4;

  /** {@code [^]} in {@code B} */
  private static final int INSIDE = 5;

  private Emptymatch scanner;

  private void scan(String input) {
    scanner = new Emptymatch(new StringReader(input));
    assertEquals(Emptymatch.YYINITIAL, scanner.yystate());
  }

  private void assertToken(int rule, String text, int offset, int state) throws IOException {
    String at = "at offset " + offset;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(offset, scanner.offset(), "offset " + at);
    assertEquals(state, scanner.yystate(), "state " + at);
  }

  /** Checks the end of input + that it does not change the lexical state. */
  private void assertEof(int state) throws IOException {
    assertEquals(Emptymatch.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
    assertEquals(state, scanner.yystate());
  }

  @Test
  public void emptyBrackets() throws Exception {
    scan("blah[]\n");
    assertToken(OPEN, "blah[", 0, Emptymatch.A);
    assertToken(EMPTY, "", 5, Emptymatch.B);
    assertToken(CLOSE, "]", 5, Emptymatch.YYINITIAL);
    assertToken(OTHER, "\n", 6, Emptymatch.YYINITIAL);
    assertEof(Emptymatch.YYINITIAL);
  }

  @Test
  public void textInBrackets() throws Exception {
    scan("foo[bar]\n\n");
    assertToken(OPEN, "foo[", 0, Emptymatch.A);
    assertToken(EMPTY, "", 4, Emptymatch.B);
    assertToken(INSIDE, "b", 4, Emptymatch.B);
    assertToken(INSIDE, "a", 5, Emptymatch.B);
    assertToken(INSIDE, "r", 6, Emptymatch.B);
    assertToken(CLOSE, "]", 7, Emptymatch.YYINITIAL);
    assertToken(OTHER, "\n", 8, Emptymatch.YYINITIAL);
    assertToken(OTHER, "\n", 9, Emptymatch.YYINITIAL);
    assertEof(Emptymatch.YYINITIAL);
  }

  /** The empty match has the text {@code ""} and the length 0, and it does not consume input. */
  @Test
  public void emptyMatchDoesNotAdvance() throws Exception {
    scan("a[[b[\n]c");
    assertToken(OPEN, "a[", 0, Emptymatch.A);
    assertToken(EMPTY, "", 2, Emptymatch.B);
    assertToken(INSIDE, "[", 2, Emptymatch.B);
    // state B has no rule for letters + "["
    assertToken(INSIDE, "b", 3, Emptymatch.B);
    assertToken(INSIDE, "[", 4, Emptymatch.B);
    assertToken(INSIDE, "\n", 5, Emptymatch.B);
    assertToken(CLOSE, "]", 6, Emptymatch.YYINITIAL);
    assertToken(OTHER, "c", 7, Emptymatch.YYINITIAL);
    assertEof(Emptymatch.YYINITIAL);
  }

  /** One empty match each time the scanner enters the state {@code A}. */
  @Test
  public void twoEmptyMatches() throws Exception {
    scan("a[]Z[]");
    assertToken(OPEN, "a[", 0, Emptymatch.A);
    assertToken(EMPTY, "", 2, Emptymatch.B);
    assertToken(CLOSE, "]", 2, Emptymatch.YYINITIAL);
    assertToken(OPEN, "Z[", 3, Emptymatch.A);
    assertToken(EMPTY, "", 5, Emptymatch.B);
    assertToken(CLOSE, "]", 5, Emptymatch.YYINITIAL);
    assertEof(Emptymatch.YYINITIAL);
  }

  /** Without letters or without {@code [} the scanner stays in {@code YYINITIAL}. */
  @Test
  public void noOpen() throws Exception {
    scan("[ab]1[");
    assertToken(OTHER, "[", 0, Emptymatch.YYINITIAL);
    assertToken(OTHER, "a", 1, Emptymatch.YYINITIAL);
    assertToken(OTHER, "b", 2, Emptymatch.YYINITIAL);
    assertToken(OTHER, "]", 3, Emptymatch.YYINITIAL);
    assertToken(OTHER, "1", 4, Emptymatch.YYINITIAL);
    assertToken(OTHER, "[", 5, Emptymatch.YYINITIAL);
    assertEof(Emptymatch.YYINITIAL);
  }

  /** EOF takes precedence over empty match. */
  @Test
  public void endOfInputBeforeEmptyMatch() throws Exception {
    scan("blah[");
    assertToken(OPEN, "blah[", 0, Emptymatch.A);
    assertEof(Emptymatch.A);
    // and again
    assertEof(Emptymatch.A);
  }

  /** End of input after the empty match, in state {@code B}. */
  @Test
  public void endOfInputInBrackets() throws Exception {
    scan("blah[x");
    assertToken(OPEN, "blah[", 0, Emptymatch.A);
    assertToken(EMPTY, "", 5, Emptymatch.B);
    assertToken(INSIDE, "x", 5, Emptymatch.B);
    assertEof(Emptymatch.B);
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof(Emptymatch.YYINITIAL);
  }
}
