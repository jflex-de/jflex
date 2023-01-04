// test: SixDigitUnicodeEscape-f
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.six_digit_unicode_escape;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Check that generation fails when a codepoint specified in {@code \UHHHHHH} format is greater than
 * the maximum codepoint for the Unicode version
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/six_digit_unicode_escape/SixDigitUnicodeEscape-f-2.flex",
    generatorThrows = GeneratorException.class,
    sysout = "javatests/de/jflex/testcase/six_digit_unicode_escape/failure2.out")
public class SixdigitunicodeescapeFailure2Test {

  @Test
  public void ok() {}
}
