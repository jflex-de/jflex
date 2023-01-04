/*
 * Copyright (C) 2009-2012 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test that a nonexistent Unicode block property character class triggers an error. */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/unicode/unicode-blocks-failure.flex",
    generatorThrows = GeneratorException.class,
    sysout = "javatests/de/jflex/testcase/unicode/unicode-blocks-failure.output")
public class UnicodeBlocksInvalidBlockTest {
  @Test
  public void expectGenerationFailure() {
    // A jUnit test must have at least one test.
  }
}
