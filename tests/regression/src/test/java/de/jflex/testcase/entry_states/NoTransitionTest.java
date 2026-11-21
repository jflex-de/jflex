/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.entry_states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * A DFA without transitions. The only rule is {@code a []}, and nothing can follow {@code a},
 * because the character class is empty.
 *
 * <p>The DFA still has the single entry state, and JFlex generates a scanner that matches no input.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/entry_states/NoTransition.flex",
    warnings = @Warn(value = ErrorMessages.NEVER_MATCH, line = 19),
    quiet = true,
    minimizedDfaStatesCount = 1,
    minimizedDfa = {"states 1, inputs 2, lexical states 1", "entry: 0 0", "0:"},
    charClassesCount = 2,
    charClasses = {"{ [0-'`']['b'-1114111] }", "{ ['a'] }"})
public class NoTransitionTest implements JFlexTest {

  @Test
  public void emptyInput() throws Exception {
    NoTransition scanner = new NoTransition(new StringReader(""));
    assertEquals(NoTransition.YYEOF, scanner.yylex());
  }

  @Test
  public void noMatch() {
    assertThrows(Error.class, new NoTransition(new StringReader("a"))::yylex);
    assertThrows(Error.class, new NoTransition(new StringReader("ab"))::yylex);
    assertThrows(Error.class, new NoTransition(new StringReader("x"))::yylex);
  }
}
