// test: semcheck
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.semcheck;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class semcheck for {@code %ignorecase} and negation operators.
 *
 * <p>See bug <a href="https://github.com/jflex-de/jflex/issues/68">#68 Cannot use lookahead with
 * ignorecase</a>
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/semcheck/semcheck.flex",
    sysout = "javatests/de/jflex/testcase/semcheck/semcheck-flex.output",
    generatorThrows = GeneratorException.class)
public class SemcheckTest {
  @Test
  public void ok() {}
}
