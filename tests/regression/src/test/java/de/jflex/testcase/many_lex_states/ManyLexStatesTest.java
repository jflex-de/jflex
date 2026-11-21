/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.many_lex_states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.IOException;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * A spec with more than 250 lexical states, so that the DFA has more entry states than its initial
 * capacity of 500 states.
 *
 * <p>The rule {@code "a"*} matches the empty string in every lexical state, which makes all entry
 * states of the DFA final. They must not lose that status when the DFA grows > 500 states.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/many_lex_states/ManyLexStates.flex",
    warnings = @Warn(value = ErrorMessages.EMPTY_MATCH, line = 45),
    quiet = true,
    minimizedDfaStatesCount = 3,
    charClassesCount = 3,
    charClasses = {"{ [0-'`']['c'-1114111] }", "{ ['a'] }", "{ ['b'] }"})
public class ManyLexStatesTest implements JFlexTest {

  /** {@code "a"*} */
  private static final int A = 1;

  /** {@code "b"} */
  private static final int B = 2;

  private ManyLexStates scanner;

  private void scan(String input, int state) {
    scanner = new ManyLexStates(new StringReader(input));
    scanner.yybegin(state);
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule in state " + scanner.yystate());
    assertEquals(text, scanner.yytext(), "text in state " + scanner.yystate());
  }

  /** Empty match in every lexical state */
  @Test
  public void emptyMatchInAllStates() throws Exception {
    int states = 0;
    for (int state = ManyLexStates.YYINITIAL; state <= ManyLexStates.S259; state += 2) {
      scan("c", state);
      assertToken(A, "");
      // no progress
      assertToken(A, "");
      states++;
    }
    assertEquals(261, states);
  }

  @Test
  public void lettersInFirstAndLastState() throws Exception {
    for (int state : new int[] {ManyLexStates.YYINITIAL, ManyLexStates.S0, ManyLexStates.S259}) {
      scan("aabab", state);
      assertToken(A, "aa");
      assertToken(B, "b");
      assertToken(A, "a");
      assertToken(B, "b");
      assertEquals(ManyLexStates.YYEOF, scanner.yylex());
      assertTrue(scanner.yyatEOF());
    }
  }
}
