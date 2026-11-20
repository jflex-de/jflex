/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.action_pipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Regression test for issue #201: {@code <<EOF>>} action did not compile when it was piped with
 * another action.
 *
 * <p>The same tests as {@link ActionPipeEofTest}, but in the {@code <<EOF>>} comes first.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/action_pipe/eof-pipe-action.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 5,
    // the same DFA for both specs of this directory
    minimizedDfa = {
      "states 5, inputs 5, lexical states 1",
      "entry: 0 0",
      "0: 0-1 -> 1; 2 -> 2; 3 -> 3; 4 -> 4",
      "1 [a0]:",
      "2 [a1]:",
      "3 [a1]: 2 -> 2",
      "4 [a2]: 0 -> 4; 4 -> 4",
      "a0: /* no action */",
      "a1: return 0;",
      "a2: return 1;"
    },
    charClassesCount = 5,
    sameCharClass = {"09\u0000\u0300", " \t.\u00A0", "aZ_$\u00E4"},
    distinctCharClasses = {"0 \n\ra"})
public class EofPipeActionTest implements JFlexTest {

  /** <code>{Identifier}</code> */
  private static final int IDENTIFIER = 1;

  /** <code>{LineTerminator}</code> or {@code <<EOF>>} */
  private static final int LINE_END = 0;

  private EofPipeAction scanner;

  private void scan(String input) {
    scanner = new EofPipeAction(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertToken(LINE_END, "");
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void tokenAtEOF() throws Exception {
    scan("ident\n\ntest");
    assertToken(IDENTIFIER, "ident");
    assertToken(LINE_END, "\n");
    assertToken(LINE_END, "\n");
    assertToken(IDENTIFIER, "test");
    assertEof();
  }

  @Test
  public void lineTerminators() throws Exception {
    scan("a\rb\r\nc\n\rd");
    assertToken(IDENTIFIER, "a");
    assertToken(LINE_END, "\r");
    assertToken(IDENTIFIER, "b");
    assertToken(LINE_END, "\r\n");
    assertToken(IDENTIFIER, "c");
    assertToken(LINE_END, "\n");
    assertToken(LINE_END, "\r");
    assertToken(IDENTIFIER, "d");
    assertEof();
  }

  @Test
  public void skipsOtherCharacters() throws Exception {
    scan(" x1 + $y. \n 2z");
    assertToken(IDENTIFIER, "x1");
    assertToken(IDENTIFIER, "$y");
    assertToken(LINE_END, "\n");
    assertToken(IDENTIFIER, "z");
    assertEof();
  }

  @Test
  public void eofActionRepeats() throws Exception {
    scan("");
    assertEof();
    assertEof();
    scan("  ");
    assertEof();
    assertEof();
  }
}
