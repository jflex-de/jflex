/*
 * Copyright 2023, Gerwin Klein.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.no_suppress_warnings;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test nevermatch warning. */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "javatests/de/jflex/testcase/no_suppress_warnings/no_suppress.flex", quiet = true)
public class NoSuppressTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
