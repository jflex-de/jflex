/*
 * Copyright 2022, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_init;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that double char class declaration fails to generate.
 *
 * <p>See https://github.com/jflex-de/jflex/issues/986
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/ccl_init/ccl2.flex",
    quiet = true,
    generatorThrows = GeneratorException.class)
public class CclDoubleTest {
  @Test
  public void ok() {}
}
