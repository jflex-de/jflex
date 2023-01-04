/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.arr_return;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/arr_return/arr.flex",
    sysout = "javatests/de/jflex/testcase/arr_return/arr-flex.output",
    minimizedDfaStatesCount = 5)
public class ArrReturnTest {
  @Test
  public void ok() {}
}
