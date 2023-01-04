// test: never
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.nevermatch;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test nevermatch warning. */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/nevermatch/never.flex",
    sysout = "javatests/de/jflex/testcase/nevermatch/expected_sysout.txt")
public class NeverTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
