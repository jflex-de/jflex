/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;

/** Tests that a cycle in the macro definitions is an error with correct error message. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/macro_exp/macro_cycle.flex",
    sysout = "src/test/java/de/jflex/testcase/macro_exp/macro_cycle.sysout",
    quiet = true,
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = MacroException.class)
public class MacroCycleFailureTest implements JFlexTest {}
