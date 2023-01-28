/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.token_limit;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test errors for {@code %token_size_limit} directive.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/issues/197">#197</a>.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/token_limit/limit4.flex",
    sysout = "javatests/de/jflex/testcase/token_limit/sys-out4.txt",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class LimitTest4 {
  @Test
  public void ok() {}
}
