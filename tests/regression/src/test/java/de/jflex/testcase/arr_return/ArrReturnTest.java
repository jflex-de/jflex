/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.arr_return;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

@TestSpec(
    lex = "src/test/java/de/jflex/testcase/arr_return/arr.flex",
    sysout = "src/test/java/de/jflex/testcase/arr_return/arr-flex.output",
    minimizedDfaStatesCount = 5)
public class ArrReturnTest implements JFlexTest {}
