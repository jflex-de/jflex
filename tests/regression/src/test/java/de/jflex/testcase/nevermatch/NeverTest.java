// test: never
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.nevermatch;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.jupiter.api.Test;

/** Test nevermatch warning. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/nevermatch/never.flex",
    sysout = "src/test/java/de/jflex/testcase/nevermatch/expected_sysout.txt")
public class NeverTest implements JFlexTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
