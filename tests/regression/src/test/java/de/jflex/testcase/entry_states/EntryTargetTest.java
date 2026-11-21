/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.entry_states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * A DFA where the only transition leads back to an entry state, and that has an entry state without
 * outgoing transitions (lexical state {@code X} has no rules).
 *
 * <p>Check that the entry states are in the DFA even without outgoing transitions.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/entry_states/EntryTarget.flex",
    warnings = @Warn(value = ErrorMessages.EMPTY_MATCH, line = 21),
    quiet = true,
    minimizedDfaStatesCount = 2,
    minimizedDfa = {
      "states 2, inputs 2, lexical states 2",
      "entry: 0 0 1 1",
      "0 [a0]: 1 -> 0",
      "1:",
      "a0: return 1;"
    },
    charClassesCount = 2,
    charClasses = {"{ [0-'`']['b'-1114111] }", "{ ['a'] }"})
public class EntryTargetTest implements JFlexTest {

  @Test
  public void initialState() throws Exception {
    EntryTarget scanner = new EntryTarget(new StringReader("aaa"));
    assertEquals(1, scanner.yylex());
    assertEquals("aaa", scanner.yytext());
    assertEquals(EntryTarget.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void emptyInput() throws Exception {
    EntryTarget scanner = new EntryTarget(new StringReader(""));
    assertEquals(EntryTarget.YYEOF, scanner.yylex());
  }

  /** Nothing matches in state {@code x} */
  @Test
  public void stateWithoutRules() throws Exception {
    EntryTarget scanner = new EntryTarget(new StringReader("a"));
    scanner.yybegin(EntryTarget.X);
    assertThrows(Error.class, scanner::yylex);

    scanner = new EntryTarget(new StringReader(""));
    scanner.yybegin(EntryTarget.X);
    assertEquals(EntryTarget.YYEOF, scanner.yylex());
  }
}
