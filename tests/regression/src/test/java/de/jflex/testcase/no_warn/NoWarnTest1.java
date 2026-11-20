/*
 * Copyright 2023, Gerwin Klein.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.no_warn;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/** Test nevermatch warning. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/no_warn/no_warn1.flex",
    quiet = true,
    warnings = @Warn(value = ErrorMessages.NEVER_MATCH, line = 20))
public class NoWarnTest1 implements JFlexTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
