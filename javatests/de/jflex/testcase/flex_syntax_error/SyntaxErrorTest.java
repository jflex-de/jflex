// test: null
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.flex_syntax_error;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that a syntax error in input does not cause a {@link NullPointerException}.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/89>#89</a> (sourceforge #1832973).
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/flex_syntax_error/null.flex",
    generatorThrows = GeneratorException.class,
    sysout = "javatests/de/jflex/testcase/flex_syntax_error/null-flex.output")
public class SyntaxErrorTest {

  @Test
  public void ok() {}
}
