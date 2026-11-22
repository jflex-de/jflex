/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_in_macro;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/1089">#1089 (lookahead in macros)</a>:
 * a macro definition that has a lookahead inside another operator is an error. A lookahead that is
 * the entire rule, also with parentheses, is allowed.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/look_in_macro/look_macro_def_neg.flex",
    sysout = "src/test/java/de/jflex/testcase/look_in_macro/look_macro_def_neg-flex.output",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class LookMacroDefNegTest implements JFlexTest {}
