/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_macros;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Test for issues <a href="https://github.com/jflex-de/jflex/issues/888">#888</a> and <a
 * href="https://github.com/jflex-de/jflex/issues/939">#939</a>: a macro inside a character class
 * that is a string ({@code M = "a"}) is an error.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_macros/ccl_macros_neg_string.flex",
    sysout = "src/test/java/de/jflex/testcase/ccl_macros/ccl_macros_neg_string-flex.output",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class CclMacrosNegStringTest implements JFlexTest {}
