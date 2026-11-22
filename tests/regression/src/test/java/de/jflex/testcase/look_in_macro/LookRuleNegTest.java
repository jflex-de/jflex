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
 * a lookahead that is not the entire expression of a rule after macro expansion is an error.
 *
 * <p>A rule whose expression is a lookahead macro cannot have the {@code |} action.
 *
 * <p>The last two rules of the spec are valid and must not be reported.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/look_in_macro/look_rule_neg.flex",
    sysout = "src/test/java/de/jflex/testcase/look_in_macro/look_rule_neg-flex.output",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class LookRuleNegTest implements JFlexTest {}
