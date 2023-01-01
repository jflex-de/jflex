/*
 * Copyright 2023, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_macros;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for issues <a href="https://github.com/jflex-de/jflex/issues/888">#888</a> and <a
 * href="https://github.com/jflex-de/jflex/issues/939">#939</a>.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/ccl_macros/ccl_macros_neg.flex",
    sysout = "javatests/de/jflex/testcase/ccl_macros/ccl_macros_neg-flex.output",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class CclMacrosNegTest {
  @Test
  public void ok() {}
}
