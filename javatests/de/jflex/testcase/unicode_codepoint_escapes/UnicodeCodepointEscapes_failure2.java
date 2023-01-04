/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode_codepoint_escapes;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.scanner.ScannerException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Check that generation fails when a codepoint specified in <code>\\u{H+}</code> format is greater
 * than the maximum codepoint for the Unicode version.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/unicode_codepoint_escapes/UnicodeCodePointEscapes-f-2.flex",
    sysout =
        "javatests/de/jflex/testcase/unicode_codepoint_escapes/UnicodeCodePointEscapes-f-2-flex.output",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class)
public class UnicodeCodepointEscapes_failure2 {
  @Test
  public void ok() {}
}
