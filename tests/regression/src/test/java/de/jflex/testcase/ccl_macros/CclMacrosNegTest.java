/*
 * Copyright 2023, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_macros;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Test for issues <a href="https://github.com/jflex-de/jflex/issues/888">#888</a> and <a
 * href="https://github.com/jflex-de/jflex/issues/939">#939</a>. A macro inside a character class
 * that is a character ({@code M = a}) must be detected as error.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_macros/ccl_macros_neg.flex",
    sysout = "src/test/java/de/jflex/testcase/ccl_macros/ccl_macros_neg-flex.output",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class CclMacrosNegTest implements JFlexTest {}
