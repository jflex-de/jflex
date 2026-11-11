// test: null
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.flex_syntax_error;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Test that a syntax error in input does not cause a {@link NullPointerException}.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/89>#89</a> (sourceforge #1832973).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/flex_syntax_error/null.flex",
    generatorThrows = GeneratorException.class,
    sysout = "src/test/java/de/jflex/testcase/flex_syntax_error/null-flex.output")
public class SyntaxErrorTest implements JFlexTest {}
