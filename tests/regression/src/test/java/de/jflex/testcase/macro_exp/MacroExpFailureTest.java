/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;

/**
 * Test <a href="https://github.com/jflex-de/jflex/issues/92>#92 Ambiguous Error Message [sf#90]</a>
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/macro_exp/macro2.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = MacroException.class)
public class MacroExpFailureTest implements JFlexTest {}
