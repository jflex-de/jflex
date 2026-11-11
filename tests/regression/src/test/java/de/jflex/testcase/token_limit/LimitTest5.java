/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.token_limit;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Test errors for {@code %token_size_limit} directive.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/issues/197">#197</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/token_limit/limit5.flex",
    sysout = "src/test/java/de/jflex/testcase/token_limit/sys-out5.txt",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class LimitTest5 implements JFlexTest {}
