/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.nevermatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * Test the never-match warning for rules with the same action.
 *
 * <p>The second {@code "a"} rule never matches should receive the warning. The {@code "b"} can
 * match and should receive no warning.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/nevermatch/SameAction.flex",
    warnings = @Warn(value = ErrorMessages.NEVER_MATCH, line = 22),
    quiet = true,
    minimizedDfaStatesCount = 3)
public class SameActionTest implements JFlexTest {

  @Test
  public void rulesWithSameAction() throws Exception {
    SameAction scanner = new SameAction(new StringReader("abc"));
    assertEquals(1, scanner.yylex());
    assertEquals("a", scanner.yytext());
    assertEquals(1, scanner.yylex());
    assertEquals("b", scanner.yytext());
    assertEquals(0, scanner.yylex());
    assertEquals("c", scanner.yytext());
    assertEquals(SameAction.YYEOF, scanner.yylex());
  }
}
