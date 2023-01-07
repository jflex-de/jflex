/*
 * Copyright 2023, Gerwin Klein.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.no_warn;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test nevermatch warning. */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/no_warn/no_warn2.flex",
    quiet = true,
    sysout = "javatests/de/jflex/testcase/no_warn/no_warn2_sysout.txt")
public class NoWarnTest2 {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
