// test: SixDigitUnicodeEscape-f
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.six_digit_unicode_escape;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Check that generation fails when a codepoint specified in {@code \UHHHHHH} format is greater than
 * the maximum codepoint for the Unicode version
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/six_digit_unicode_escape/SixDigitUnicodeEscape-f.flex",
    generatorThrows = GeneratorException.class,
    sysout = "src/test/java/de/jflex/testcase/six_digit_unicode_escape/failure.out")
public class SixdigitunicodeescapeFailureTest implements JFlexTest {}
