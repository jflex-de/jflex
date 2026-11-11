/*
 * Copyright (C) 2009-2012 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.jupiter.api.Test;

/** Test that a nonexistent Unicode block property character class triggers an error. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/unicode/unicode-blocks-failure.flex",
    generatorThrows = GeneratorException.class,
    sysout = "src/test/java/de/jflex/testcase/unicode/unicode-blocks-failure.output")
public class UnicodeBlocksInvalidBlockTest implements JFlexTest {
  @Test
  public void expectGenerationFailure() {
    // A jUnit test must have at least one test.
  }
}
