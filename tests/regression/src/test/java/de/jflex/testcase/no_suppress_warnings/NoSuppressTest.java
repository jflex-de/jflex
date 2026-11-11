/*
 * Copyright 2023, Gerwin Klein.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.no_suppress_warnings;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.jupiter.api.Test;

/** Test nevermatch warning. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/no_suppress_warnings/no_suppress.flex",
    quiet = true)
public class NoSuppressTest implements JFlexTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
