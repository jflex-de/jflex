// test: never
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.nevermatch;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/** Test nevermatch warning. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/nevermatch/never.flex",
    warnings = {
      @Warn(value = ErrorMessages.NEVER_MATCH, line = 20),
      @Warn(value = ErrorMessages.NEVER_MATCH, line = 26)
    },
    sysout = "src/test/java/de/jflex/testcase/nevermatch/expected_sysout.txt",
    minimizedDfaStatesCount = 4)
public class NeverTest implements JFlexTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
