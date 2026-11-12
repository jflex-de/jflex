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
    minimizedDfaStatesCount = 5,
    charClassesCount = 4,
    charClasses = {
      "{ [0-'a']['c'-'k']['m'-'t']['v'-1114111] }",
      "{ ['b'] }",
      "{ ['l'] }",
      "{ ['u'] }"
    },
    sameCharClass = {"az\uD83D\uDE00", "bb"},
    distinctCharClasses = {"blua"})
public class ArrReturnTest implements JFlexTest {}
